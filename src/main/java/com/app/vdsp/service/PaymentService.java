package com.app.vdsp.service;

import com.app.vdsp.entity.Payment;

import java.util.Map;

public interface PaymentService {
    String processPaymentNotification(Map<String, String> params);
    boolean isAlreadyPaid(Long reservationId, String authHeader);
}
