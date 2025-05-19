package com.app.vdsp.controller;

import com.app.vdsp.dto.DashboardStatsDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.service.AdminDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/dashboard")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getDashboardStats(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(adminDashboardService.getDashboardStats(authHeader, year, month));
    }
}
