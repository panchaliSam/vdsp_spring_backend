package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.PaymentDto;
import com.app.vdsp.entity.Payment;
import com.app.vdsp.repository.PaymentRepository;
import com.app.vdsp.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public String processPaymentNotification(Payment payment) {
        // Save payment details to the database
        paymentRepository.save(payment);

        // Return a message based on the payment status
        switch (payment.getPaymentStatus()) {
            case SUCCESS:
                return "Payment Successful";
            case PENDING:
                return "Payment Pending";
            case FAILED:
                return "Payment Failed";
            case CANCELED:
                return "Payment Canceled";
            default:
                return "Unknown Payment Status";
        }
    }
}
