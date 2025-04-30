package com.app.vdsp.service;

import com.app.vdsp.dto.PaymentDto;
import com.app.vdsp.type.PaymentStatus;

public interface PaymentService {
    PaymentDto createPayment(PaymentDto paymentDto);
    void updatePaymentStatus(Long paymentId, PaymentStatus status);
}
