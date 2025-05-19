package com.app.vdsp.service;

import com.app.vdsp.dto.DashboardStatsDto;
import com.app.vdsp.entity.ApiResponse;

import java.math.BigDecimal;

public interface AdminDashboardService {
    ApiResponse<BigDecimal> getTotalSuccessfulPayments(String authHeader);
    ApiResponse<Long> getTotalEvents(String authHeader);
    ApiResponse<Long> getTotalEventsByYear(int year, String authHeader);
    ApiResponse<Long> getTotalEventsByMonth(int year, int month, String authHeader);
    ApiResponse<Long> getTotalStaff(String authHeader);
    ApiResponse<Long> getTotalCustomers(String authHeader);
    ApiResponse<DashboardStatsDto> getDashboardStats(String authHeader, int year, int month);
}
