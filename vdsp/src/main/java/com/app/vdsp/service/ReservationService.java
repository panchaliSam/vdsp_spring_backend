package com.app.vdsp.service;

import com.app.vdsp.dto.ReservationDto;
import com.app.vdsp.entity.Reservation;

import java.util.List;

public interface ReservationService {
    ReservationDto createReservation(ReservationDto reservationDto);
    List<Reservation> getAllReservations();
    Reservation getReservationById(Long id);
    void updateReservation(ReservationDto reservationDto);
}
