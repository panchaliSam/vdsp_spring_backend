package com.app.vdsp.service;

import com.app.vdsp.dto.PaymentHistoryDto;
import com.app.vdsp.entity.ApiResponse;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentHistoryService {
    ApiResponse<List<PaymentHistoryDto>> getUserPaymentHistory(String authHeader);
    ApiResponse<List<PaymentHistoryDto>> getAllPayments(String authHeader);
}