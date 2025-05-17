package com.app.vdsp.controller;

import com.app.vdsp.dto.PaymentHistoryDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.service.PaymentHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/paymentHistory")
public class PaymentHistoryController {

    private final PaymentHistoryService paymentHistoryService;

    public PaymentHistoryController(PaymentHistoryService paymentHistoryService) {
        this.paymentHistoryService = paymentHistoryService;
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<PaymentHistoryDto>>> getUserPaymentHistory(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(paymentHistoryService.getUserPaymentHistory(authHeader));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<PaymentHistoryDto>>> getAllPayments(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(paymentHistoryService.getAllPayments(authHeader));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/total-success")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalSuccessfulPayments(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(paymentHistoryService.getTotalSuccessfulPayments(authHeader));
    }
}