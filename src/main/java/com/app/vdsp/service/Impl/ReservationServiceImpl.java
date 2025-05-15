package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.ReservationDto;
import com.app.vdsp.entity.Reservation;
import com.app.vdsp.entity.ReservationApproval;
import com.app.vdsp.entity.User;
import com.app.vdsp.entity.ReservationPackage;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.helpers.SessionHelper;
import com.app.vdsp.repository.PackageRepository;
import com.app.vdsp.repository.ReservationApprovalRepository;
import com.app.vdsp.repository.ReservationRepository;
import com.app.vdsp.repository.UserRepository;
import com.app.vdsp.service.ReservationService;
import com.app.vdsp.type.SessionType;
import com.app.vdsp.utils.JWTService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final JWTService jwtService;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final PackageRepository packageRepository;
    private final ReservationApprovalRepository reservationApprovalRepository;

    public ReservationServiceImpl(JWTService jwtService, ReservationRepository reservationRepository, UserRepository userRepository, PackageRepository packageRepository, ReservationApprovalRepository reservationApprovalRepository) {
        this.jwtService = jwtService;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.packageRepository = packageRepository;
        this.reservationApprovalRepository = reservationApprovalRepository;
    }

    @Transactional
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

            ReservationPackage eventPackage = packageRepository.findByName(reservationDto.getPackageName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found"));

            SessionType sessionType = SessionHelper.calculateSessionType(
                    reservationDto.getEventStartTime(),
                    reservationDto.getEventEndTime()
            );

            SessionHelper.validateSessionType(sessionType, reservationDto.getEventDate(), reservationRepository);

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

            List<SessionType> conflictTypes;

            switch (sessionType) {
                case FULLDAY_SESSION -> conflictTypes = List.of(SessionType.FULLDAY_SESSION, SessionType.MORNING_SESSION, SessionType.EVENING_SESSION);
                case MORNING_SESSION -> conflictTypes = List.of(SessionType.FULLDAY_SESSION, SessionType.MORNING_SESSION);
                case EVENING_SESSION -> conflictTypes = List.of(SessionType.FULLDAY_SESSION, SessionType.EVENING_SESSION);
                default -> throw new IllegalStateException("Unexpected value: " + sessionType);
            }

            List<Reservation> conflicts = reservationRepository.findConflictingReservationsWithLock(
                    reservationDto.getEventDate(), conflictTypes
            );

            if (!conflicts.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Time slot already reserved");
            }

            reservationRepository.save(reservation);

            ReservationApproval approval = ReservationApproval.builder()
                    .reservation(reservation)
                    .user(user)
                    .status(false)
                    .build();

            reservationApprovalRepository.save(approval);

            reservationDto.setCustomerName(user.getFirstName() + " " + user.getLastName());
            reservationDto.setSessionType(sessionType);
            reservationDto.setPackageId(eventPackage.getId());
            reservationDto.setPackageName(eventPackage.getName());
            reservationDto.setPriceAmount(eventPackage.getPrice());

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

    @Override
    public List<ReservationDto> getAllReservations() {
        try {
            List<Reservation> reservations = reservationRepository.findAll();
            log.info("All reservations: {}", reservations);

            return reservations.stream()
                    .map(reservation -> {
                        ReservationPackage reservationPackage = reservation.getEventPackage();
                        return ReservationDto.fromEntity(reservation, reservationPackage);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Unexpected error while getting all reservations", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error occurred while getting all reservations");
        }
    }

    @Override
    public Optional<ReservationDto> getReservationById(Long id) {
        try {
            Optional<Reservation> reservation = reservationRepository.findById(id);
            if (reservation.isPresent()) {
                log.info("Reservation found: {}", reservation.get());

                ReservationPackage reservationPackage = reservation.get().getEventPackage();
                return Optional.of(ReservationDto.fromEntity(reservation.get(), reservationPackage));
            } else {
                log.warn("Reservation not found for ID: {}", id);
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Unexpected error while fetching reservation with ID: {}", id, e);
            throw new RuntimeException("Unexpected error occurred while fetching reservation", e);
        }
    }


    @Override
    public Map<String, List<LocalDate>> getReservedDates(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        List<Reservation> reservations = reservationRepository.findAll();

        List<LocalDate> fullDaySessions = reservations.stream()
                .filter(reservation -> reservation.getSessionType() == SessionType.FULLDAY_SESSION)
                .map(Reservation::getEventDate)
                .distinct()
                .collect(Collectors.toList());

        List<LocalDate> partialSessions = reservations.stream()
                .filter(reservation -> reservation.getSessionType() == SessionType.MORNING_SESSION ||
                        reservation.getSessionType() == SessionType.EVENING_SESSION)
                .collect(Collectors.groupingBy(Reservation::getEventDate, Collectors.toList()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() == 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Map<String, List<LocalDate>> reservedDates = new HashMap<>();
        reservedDates.put("fullDay", fullDaySessions);
        reservedDates.put("partial", partialSessions);

        return reservedDates;
    }

}
