package com.app.vdsp.repository;

import com.app.vdsp.entity.Payment;
import com.app.vdsp.type.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByPaymentId(String paymentId);
    boolean existsByReservationIdAndPaymentStatus(Long reservationId, PaymentStatus status);
}