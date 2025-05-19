package com.app.vdsp.service;

import com.app.vdsp.dto.DashboardStatsDto;
import com.app.vdsp.entity.ApiResponse;

public interface AdminDashboardService {
    ApiResponse<DashboardStatsDto> getDashboardStats(String authHeader, int year, int month);
}
