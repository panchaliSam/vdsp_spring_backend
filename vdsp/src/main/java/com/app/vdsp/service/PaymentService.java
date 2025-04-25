package com.app.vdsp.service;

import com.app.vdsp.entity.Payment;
import com.app.vdsp.type.PaymentStatus;

import java.math.BigDecimal;

public interface PaymentService {
    Payment createPayment(Long reservationId, Long userId, BigDecimal amount, String paymentMethod);
    void updatePaymentStatus(Long paymentId, PaymentStatus status);
}