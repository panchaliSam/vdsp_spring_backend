package com.app.vdsp.service;

import com.app.vdsp.entity.Payment;

public interface PaymentService {
    String processPaymentNotification(Payment payment);
}
