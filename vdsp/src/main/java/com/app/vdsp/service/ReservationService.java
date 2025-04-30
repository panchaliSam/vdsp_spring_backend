package com.app.vdsp.service;

import com.app.vdsp.dto.ReservationDto;
import com.app.vdsp.entity.Reservation;

import java.util.List;

public interface ReservationService {
    ReservationDto createReservation(ReservationDto reservationDto, String authorizationHeader);
    List<ReservationDto> getAllReservations();
    Reservation getReservationById(Long id);
}
