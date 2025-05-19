package com.app.vdsp.service;

import com.app.vdsp.dto.PaymentHistoryDto;
import com.app.vdsp.entity.ApiResponse;

import java.math.BigDecimal;
import java.util.List;

public interface AdminDashboardService {
    ApiResponse<BigDecimal> getTotalSuccessfulPayments(String authHeader);

}
