package com.app.vdsp.service;

import com.app.vdsp.entity.ApiResponse;

import java.util.Map;

public interface PaymentService {
    ApiResponse<String> processPaymentNotification(Map<String, String> params);
    ApiResponse<Boolean> isAlreadyPaid(Long reservationId, String authHeader);
}