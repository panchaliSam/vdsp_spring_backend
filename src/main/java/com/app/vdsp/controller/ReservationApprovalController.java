package com.app.vdsp.controller;

import com.app.vdsp.dto.ReservationApprovalDto;
import com.app.vdsp.entity.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<ReservationApprovalDto>>> getAllReservationApprovals() {
        try {
            log.info("Fetching all reservation approvals");
            ApiResponse<List<ReservationApprovalDto>> response = reservationApprovalService.getAllReservationApprovals();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error while fetching reservation approvals", e);
            throw new ResponseStatusException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch reservation approvals");
        }
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/reservationApproval")
    public ResponseEntity<ApiResponse<List<ReservationApprovalDto>>> getApprovedReservations(@RequestHeader("Authorization") String authorizationHeader) {
        ApiResponse<List<ReservationApprovalDto>> response = reservationApprovalService.getApprovedReservations(authorizationHeader);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ReservationApprovalDto>> updateApprovalStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody) {
        try {
            log.info("Patching status for reservation approval with ID: {}", id);

            String status = requestBody.get("status");
            if (status == null || status.isBlank()) {
                log.error("Status is missing in the request body");
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Status is required", null));
            }

            ApprovalStatus approvalStatus;
            try {
                approvalStatus = ApprovalStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.error("Invalid approval status: {}", status, e);
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Invalid status value", null));
            }

            ApiResponse<ReservationApprovalDto> response = reservationApprovalService.updateApprovalStatus(id, approvalStatus);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            log.error("Business error while patching reservation status", e);
            return ResponseEntity.status(e.getStatusCode()).body(new ApiResponse<>(false, e.getReason(), null));
        } catch (Exception e) {
            log.error("Unexpected error while patching reservation status", e);
            return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "Unexpected error occurred", null));
        }
    }
}