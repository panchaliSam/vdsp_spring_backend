package com.app.vdsp.service;

import com.app.vdsp.dto.PaymentDto;
import com.app.vdsp.type.PaymentStatus;
import org.springframework.ui.Model;

public interface PaymentService {
    String createPayment(String orderId, double amount, Model model);
    void updatePaymentStatus(Long paymentId, PaymentStatus status);
}
