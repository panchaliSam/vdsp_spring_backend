package com.app.vdsp.controller;

import com.app.vdsp.dto.ReservationApprovalDto;
import com.app.vdsp.service.ReservationApprovalService;
import com.app.vdsp.type.ApprovalStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{id}/status")
    public ResponseEntity<ReservationApprovalDto> updateReservationApprovalStatus(
            @PathVariable Long id,
            @RequestBody Map<String, ApprovalStatus> requestBody) {
        ApprovalStatus status = requestBody.get("status");
        ReservationApprovalDto updatedApproval = reservationApprovalService.updateApprovalStatus(id, status);
        return ResponseEntity.ok(updatedApproval);
    }

}
