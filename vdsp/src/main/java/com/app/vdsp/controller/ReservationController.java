package com.app.vdsp.controller;

import com.app.vdsp.dto.ReservationDto;
import com.app.vdsp.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getAll")
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        List<ReservationDto> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }
}
