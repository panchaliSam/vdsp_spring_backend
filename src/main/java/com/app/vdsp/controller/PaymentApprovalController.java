package com.app.vdsp.controller;

import com.app.vdsp.dto.PaymentApprovalDto;
import com.app.vdsp.service.PaymentApprovalService;
import com.app.vdsp.type.ApprovalStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/paymentApprovals")
@RequiredArgsConstructor
public class PaymentApprovalController {

    private static final Logger log = LoggerFactory.getLogger(PaymentApprovalController.class);
    private final PaymentApprovalService paymentApprovalService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<List<PaymentApprovalDto>> getAllApprovals() {
        log.info("Fetching all payment approvals");
        return ResponseEntity.ok(paymentApprovalService.getAllPaymentApprovals());
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/myPaymentApprovals")
    public ResponseEntity<List<PaymentApprovalDto>> getMyApprovals(@RequestHeader("Authorization") String authHeader) {
        log.info("Fetching payment approvals for current user");
        return ResponseEntity.ok(paymentApprovalService.getApprovedPayments(authHeader));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentApprovalDto> updateApprovalStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody) {

        String statusStr = requestBody.get("status");
        if (statusStr == null || statusStr.isBlank()) {
            log.error("Status is missing in request body");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is missing");
        }

        ApprovalStatus status;
        try {
            status = ApprovalStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid status value: {}", statusStr);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status value", e);
        }

        log.info("Updating payment approval status for ID {} to {}", id, status);
        PaymentApprovalDto updated = paymentApprovalService.updateApprovalStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}