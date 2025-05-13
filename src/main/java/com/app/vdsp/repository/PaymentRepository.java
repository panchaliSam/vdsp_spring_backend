package com.app.vdsp.repository;

import com.app.vdsp.dto.PaymentDto;
import com.app.vdsp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByPaymentId(String paymentId);
}