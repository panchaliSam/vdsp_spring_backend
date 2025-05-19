package com.app.vdsp.controller;

import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.service.AdminDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/dashboard")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/total-success")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalSuccessfulPayments(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(adminDashboardService.getTotalSuccessfulPayments(authHeader));
    }
}
