package com.app.vdsp.controller;

import com.app.vdsp.dto.ReservationApprovalDto;
import com.app.vdsp.dto.ReservationDto;
import com.app.vdsp.service.ReservationApprovalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservationApprovals")
public class ReservationApprovalController {

    private static final Logger log = LoggerFactory.getLogger(ReservationApprovalController.class);

    private final ReservationApprovalService reservationApprovalService;

    public ReservationApprovalController(ReservationApprovalService reservationApprovalService) {
        this.reservationApprovalService = reservationApprovalService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<List<ReservationApprovalDto>> getAllReservationApprovals() {
        try {
            log.info("Fetching all reservation approvals");
            List<ReservationApprovalDto> reservationApprovals = reservationApprovalService.getAllReservationApprovals();
            return ResponseEntity.ok(reservationApprovals);
        } catch (Exception e) {
            log.error("Error while fetching reservation approvals", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/reservationApproval")
    public ResponseEntity<List<ReservationDto>> getApprovedReservations(@RequestHeader("Authorization") String authorizationHeader) {
        List<ReservationDto> approvedReservations = reservationApprovalService.getApprovedReservations(authorizationHeader);
        return ResponseEntity.ok(approvedReservations);
    }
}
