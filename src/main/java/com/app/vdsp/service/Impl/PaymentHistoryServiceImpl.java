package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.PaymentHistoryDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.PaymentRepository;
import com.app.vdsp.service.PaymentHistoryService;
import com.app.vdsp.utils.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentHistoryServiceImpl implements PaymentHistoryService {

    private final PaymentRepository paymentRepository;
    private final JWTService jwtService;

    @Override
    public ApiResponse<List<PaymentHistoryDto>> getUserPaymentHistory(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        Long userId = jwtService.extractUserId(authHeader.substring(7));

        List<PaymentHistoryDto> history = paymentRepository.findAll().stream()
                .filter(p -> p.getReservation().getUser().getId().equals(userId))
                .map(PaymentHistoryDto::fromEntity)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Fetched user payment history", history);
    }

    @Override
    public ApiResponse<List<PaymentHistoryDto>> getAllPayments(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        List<PaymentHistoryDto> history = paymentRepository.findAll().stream()
                .map(PaymentHistoryDto::fromEntity)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Fetched all payments", history);
    }

}