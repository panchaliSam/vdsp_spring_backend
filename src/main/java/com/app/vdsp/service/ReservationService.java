package com.app.vdsp.service;

import com.app.vdsp.dto.ReservationDto;
import com.app.vdsp.entity.ApiResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReservationService {
    ApiResponse<ReservationDto> createReservation(ReservationDto reservationDto, String authorizationHeader);
    ApiResponse<List<ReservationDto>> getAllReservations();
    ApiResponse<ReservationDto> getReservationById(Long id);
    ApiResponse<Map<String, List<LocalDate>>> getReservedDates(String authHeader);
}