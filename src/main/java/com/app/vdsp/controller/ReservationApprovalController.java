package com.app.vdsp.controller;

import com.app.vdsp.dto.ReservationApprovalDto;
import com.app.vdsp.service.ReservationApprovalService;
import com.app.vdsp.type.ApprovalStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<ReservationApprovalDto>> getApprovedReservations(@RequestHeader("Authorization") String authorizationHeader) {
        List<ReservationApprovalDto> approvedReservations = reservationApprovalService.getApprovedReservations(authorizationHeader);
        return ResponseEntity.ok(approvedReservations);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ReservationApprovalDto> updateApprovalStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody) {
        try {
            log.info("Patching status for reservation approval with ID: {}", id);

            String status = requestBody.get("status");
            if (status == null || status.isBlank()) {
                log.error("Status is missing in the request body");
                return ResponseEntity.badRequest().body(null);
            }
            ApprovalStatus approvalStatus;
            try {
                approvalStatus = ApprovalStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.error("Invalid approval status: {}", status, e);
                return ResponseEntity.badRequest().body(null);
            }
            ReservationApprovalDto updatedApproval = reservationApprovalService.updateApprovalStatus(id, approvalStatus);
            return ResponseEntity.ok(updatedApproval);
        } catch (ResponseStatusException e) {
            log.error("Business error while patching reservation status", e);
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            log.error("Unexpected error while patching reservation status", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
