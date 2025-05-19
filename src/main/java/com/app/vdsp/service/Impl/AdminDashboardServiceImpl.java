package com.app.vdsp.service.Impl;

import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.entity.Payment;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.PaymentRepository;
import com.app.vdsp.service.AdminDashboardService;
import com.app.vdsp.type.PaymentStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final PaymentRepository paymentRepository;

    public AdminDashboardServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public ApiResponse<BigDecimal> getTotalSuccessfulPayments(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        List<Payment> successfulPayments = paymentRepository.findAll().stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.SUCCESS)
                .toList();

        BigDecimal total = successfulPayments.stream()
                .map(Payment::getPayhereAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ApiResponse<>(true, "Total successful payments calculated", total);
    }
}
