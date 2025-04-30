package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.ReservationDto;
import com.app.vdsp.entity.Reservation;
import com.app.vdsp.service.ReservationService;
import com.app.vdsp.utils.JWTService;

import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    private final JWTService jwtService;

    public ReservationServiceImpl(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public ReservationDto createReservation(ReservationDto reservationDto) {
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
