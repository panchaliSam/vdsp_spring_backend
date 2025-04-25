package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.PaymentDto;
import com.app.vdsp.entity.Payment;
import com.app.vdsp.entity.Reservation;
import com.app.vdsp.entity.User;
import com.app.vdsp.repository.PaymentRepository;
import com.app.vdsp.repository.ReservationRepository;
import com.app.vdsp.repository.UserRepository;
import com.app.vdsp.service.PaymentService;
import com.app.vdsp.type.PaymentStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @Value("${payhere.merchant_id}")
    private String merchantId;

    @Value("${payhere.secret_key}")
    private String secretKey;

    @Value("${payhere.sandbox_url}")
    private String sandboxUrl;

    @Value("${payhere.return_url}")
    private String returnUrl;

    @Value("${payhere.cancel_url}")
    private String cancelUrl;

    @Value("${payhere.notify_url}")
    private String notifyUrl;

    public PaymentServiceImpl(PaymentRepository paymentRepository, UserRepository userRepository, ReservationRepository reservationRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) {
        User user = userRepository.findById(paymentDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reservation reservation = reservationRepository.findById(paymentDto.getReservationId())
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        Payment payment = Payment.builder()
                .reservation(reservation)
                .user(user)
                .paymentAmount(paymentDto.getPaymentAmount())
                .paymentMethod(paymentDto.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .paymentUrl(sandboxUrl + "pay")
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        return mapToDto(savedPayment);
    }

    @Override
    public void updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setPaymentStatus(status);
        paymentRepository.save(payment);
    }

    private PaymentDto mapToDto(Payment payment) {
        return PaymentDto.builder()
                .reservationId(payment.getReservation().getId())
                .userId(payment.getUser().getId())
                .paymentStatus(payment.getPaymentStatus())
                .paymentUrl(payment.getPaymentUrl())
                .paymentAmount(payment.getPaymentAmount())
                .paymentMethod(payment.getPaymentMethod())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
