package com.app.vdsp.service;

import com.app.vdsp.dto.ReservationDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReservationService {
    ReservationDto createReservation(ReservationDto reservationDto, String authorizationHeader);
    List<ReservationDto> getAllReservations();
    Optional<ReservationDto> getReservationById(Long id);
    Map<String, List<LocalDate>> getReservedDates(String authHeader);
}
