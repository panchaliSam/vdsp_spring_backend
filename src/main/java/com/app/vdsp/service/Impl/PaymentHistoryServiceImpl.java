package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.PaymentHistoryDto;
import com.app.vdsp.entity.Payment;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.PaymentRepository;
import com.app.vdsp.service.PaymentHistoryService;
import com.app.vdsp.type.PaymentStatus;
import com.app.vdsp.utils.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentHistoryServiceImpl implements PaymentHistoryService {

    private final PaymentRepository paymentRepository;
    private final JWTService jwtService;

    @Override
    public List<PaymentHistoryDto> getUserPaymentHistory(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        Long userId = jwtService.extractUserId(authHeader.substring(7));

        return paymentRepository.findAll().stream()
                .filter(p -> p.getReservation().getUser().getId().equals(userId))
                .map(PaymentHistoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentHistoryDto> getAllPayments(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        List<Payment> payments = paymentRepository.findAll();

        return payments.stream()
                .map(PaymentHistoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalSuccessfulPayments(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        List<Payment> successfulPayments = paymentRepository.findAll().stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.SUCCESS)
                .toList();

        return successfulPayments.stream()
                .map(Payment::getPayhereAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}