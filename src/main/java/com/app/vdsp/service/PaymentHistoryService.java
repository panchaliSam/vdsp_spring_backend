package com.app.vdsp.service;

import com.app.vdsp.dto.PaymentHistoryDto;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentHistoryService {
    List<PaymentHistoryDto> getUserPaymentHistory(String authHeader);
    List<PaymentHistoryDto> getAllPayments(String authHeader);
    BigDecimal getTotalSuccessfulPayments(String authHeader);
}