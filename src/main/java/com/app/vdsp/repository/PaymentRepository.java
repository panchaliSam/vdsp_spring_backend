package com.app.vdsp.repository;

import com.app.vdsp.entity.Payment;
import com.app.vdsp.type.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByPaymentId(String paymentId);
    boolean existsByReservationIdAndPaymentStatus(Long reservationId, PaymentStatus status);
    Payment findPaymentByReservation_Id(Long reservationId);
}