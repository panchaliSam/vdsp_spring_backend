package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.ReservationDto;
import com.app.vdsp.entity.Reservation;
import com.app.vdsp.entity.User;
import com.app.vdsp.entity.Package;
import com.app.vdsp.repository.PackageRepository;
import com.app.vdsp.repository.ReservationRepository;
import com.app.vdsp.repository.UserRepository;
import com.app.vdsp.service.ReservationService;
import com.app.vdsp.type.SessionType;
import com.app.vdsp.utils.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final JWTService jwtService;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final PackageRepository packageRepository;

    public ReservationServiceImpl(JWTService jwtService, ReservationRepository reservationRepository, UserRepository userRepository, PackageRepository packageRepository) {
        this.jwtService = jwtService;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.packageRepository = packageRepository;
    }

    @Override
    public ReservationDto createReservation(ReservationDto reservationDto, String authorizationHeader) {
        log.info("Creating reservation with authorization header: {}", authorizationHeader);

        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is missing or invalid");
            }

            String token = authorizationHeader.substring(7);

            Long userId = jwtService.extractUserId(token);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            Package eventPackage = packageRepository.findById(reservationDto.getPackageId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found"));

            SessionType sessionType = calculateSessionType(
                    reservationDto.getEventStartTime(),
                    reservationDto.getEventEndTime()
            );

            validateSessionType(sessionType, reservationDto.getEventDate());

            Reservation reservation = new Reservation();
            reservation.setUser(user);
            reservation.setEventType(reservationDto.getEventType());
            reservation.setEventPackage(eventPackage);
            reservation.setEventLocation(reservationDto.getEventLocation());
            reservation.setEventDate(reservationDto.getEventDate());
            reservation.setEventStartTime(reservationDto.getEventStartTime());
            reservation.setEventEndTime(reservationDto.getEventEndTime());
            reservation.setSessionType(sessionType);
            reservation.setCreatedAt(LocalDateTime.now());
            reservation.setUpdatedAt(LocalDateTime.now());

            reservationRepository.save(reservation);

            reservationDto.setCustomerName(user.getFirstName() + " " + user.getLastName());
            reservationDto.setSessionType(sessionType);

            log.info("Reservation created: {}", reservation);
            return reservationDto;
        } catch (ResponseStatusException e) {
            log.error("Business error: {}", e.getReason(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while creating reservation", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred while creating reservation", e);
        }
    }

    private void validateSessionType(SessionType sessionType, LocalDate eventDate) {
        List<Reservation> existingReservations = reservationRepository.findByEventDate(eventDate);

        for (Reservation reservation : existingReservations) {
            if (reservation.getSessionType() == SessionType.FULLDAY_SESSION) {
                log.info("Reservation with session type {} is already full session", sessionType);
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "A full-day session is already reserved for this date. No further reservations are allowed.");
            }
            if (sessionType == SessionType.FULLDAY_SESSION) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Cannot create a full-day session as morning or evening sessions already exist for this date.");
            }
            if ((sessionType == SessionType.MORNING_SESSION && reservation.getSessionType() == SessionType.EVENING_SESSION) ||
                    (sessionType == SessionType.EVENING_SESSION && reservation.getSessionType() == SessionType.MORNING_SESSION)) {
                continue;
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Invalid session type. Morning and evening sessions cannot overlap or conflict.");
            }
        }
    }

    private SessionType calculateSessionType(LocalTime startTime, LocalTime endTime) {
        long duration = java.time.Duration.between(startTime, endTime).toHours();

        if (duration >= 6) {
            return SessionType.FULLDAY_SESSION;
        } else if (startTime.isBefore(LocalTime.NOON)) {
            return SessionType.MORNING_SESSION;
        } else {
            return SessionType.EVENING_SESSION;
        }
    }

    @Override
    public List<Reservation> getAllReservations() {
        return List.of();
    }

    @Override
    public Reservation getReservationById(Long id) {
        return null;
    }

    @Override
    public void updateReservation(ReservationDto reservationDto) {

    }
}
