package com.app.vdsp.controller;

import com.app.vdsp.dto.ReservationDto;
import com.app.vdsp.service.ReservationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("/create")
    public ResponseEntity<ReservationDto> createReservation(
            @RequestBody ReservationDto reservationDto,
            @RequestHeader("Authorization") String authorizationHeader) {
        ReservationDto createdReservation = reservationService.createReservation(reservationDto, authorizationHeader);
        return ResponseEntity.ok(createdReservation);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/getAll")
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        List<ReservationDto> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable("id") Long id) {
        Optional<ReservationDto> reservation = reservationService.getReservationById(id);

        return reservation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null));
    }

    @GetMapping("/dates/reserved")
    public ResponseEntity<Map<String, List<LocalDate>>> getReservedDates(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        Map<String, List<LocalDate>> reservedDates = reservationService.getReservedDates(authHeader);
        return ResponseEntity.ok(reservedDates);
    }

}
