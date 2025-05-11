package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.PaymentDto;
import com.app.vdsp.service.PaymentService;
import com.app.vdsp.type.PaymentStatus;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) {
        return null;
    }

    @Override
    public void updatePaymentStatus(Long paymentId, PaymentStatus status) {

    }
}
