package com.app.vdsp.controller;

import com.app.vdsp.dto.ReservationDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.service.ReservationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ReservationDto>> createReservation(
            @RequestBody ReservationDto reservationDto,
            @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(reservationService.createReservation(reservationDto, authorizationHeader));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<ReservationDto>>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationDto>> getReservationById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @GetMapping("/dates/reserved")
    public ResponseEntity<ApiResponse<Map<String, List<LocalDate>>>> getReservedDates(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        return ResponseEntity.ok(reservationService.getReservedDates(authHeader));
    }
}