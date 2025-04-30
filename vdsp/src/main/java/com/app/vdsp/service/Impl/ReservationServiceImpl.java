package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.ReservationDto;
import com.app.vdsp.entity.Reservation;
import com.app.vdsp.entity.User;
import com.app.vdsp.entity.Package;
import com.app.vdsp.repository.PackageRepository;
import com.app.vdsp.repository.ReservationRepository;
import com.app.vdsp.repository.UserRepository;
import com.app.vdsp.service.ReservationService;
import com.app.vdsp.utils.JWTService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

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
    public ReservationDto createReservation(ReservationDto reservationDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwtService.extractUserId(userDetails.getUsername());

        reservationDto.setUserId(userId);

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Package eventPackage = packageRepository.findById(reservationDto.getPackageId())
                .orElseThrow(() -> new RuntimeException("Package not found"));

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setEventType(reservationDto.getEventType());
        reservation.setEventPackage(eventPackage);
        reservation.setEventLocation(reservationDto.getEventLocation());
        reservation.setEventDate(reservationDto.getEventDate());
        reservation.setEventStartTime(reservationDto.getEventStartTime());
        reservation.setEventEndTime(reservationDto.getEventEndTime());
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        reservationRepository.save(reservation);

        return reservationDto;
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
