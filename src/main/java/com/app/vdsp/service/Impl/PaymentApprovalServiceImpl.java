package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.PaymentApprovalDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.entity.PaymentApproval;
import com.app.vdsp.repository.PaymentApprovalRepository;
import com.app.vdsp.service.PaymentApprovalService;
import com.app.vdsp.type.ApprovalStatus;
import com.app.vdsp.utils.JWTService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentApprovalServiceImpl implements PaymentApprovalService {

    private static final Logger log = LoggerFactory.getLogger(PaymentApprovalServiceImpl.class);
    private final PaymentApprovalRepository paymentApprovalRepository;
    private final JWTService jwtService;

    @Override
    public ApiResponse<List<PaymentApprovalDto>> getAllPaymentApprovals() {
        try {
            List<PaymentApproval> paymentApprovals = paymentApprovalRepository.findAll();
            List<PaymentApprovalDto> result = paymentApprovals.stream()
                    .map(PaymentApprovalDto::fromEntity)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Fetched all payment approvals", result);
        } catch (Exception e) {
            log.error("Error while fetching approvals", e);
            return new ApiResponse<>(false, "Error while fetching approvals", null);
        }
    }

    @Override
    public ApiResponse<List<PaymentApprovalDto>> getApprovedPayments(String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return new ApiResponse<>(false, "Authorization header is missing or invalid", null);
            }

            Long userId = jwtService.extractUserId(authorizationHeader.substring(7));
            List<PaymentApproval> approvals = paymentApprovalRepository.findByUserId(userId);

            List<PaymentApprovalDto> result = approvals.stream()
                    .map(PaymentApprovalDto::fromEntity)
                    .collect(Collectors.toList());

            return new ApiResponse<>(true, "Fetched approved payments for user", result);
        } catch (Exception e) {
            log.error("Failed to get user payments", e);
            return new ApiResponse<>(false, "Failed to get user payments", null);
        }
    }

    @Override
    public ApiResponse<PaymentApprovalDto> updateApprovalStatus(Long id, ApprovalStatus status) {
        try {
            PaymentApproval approval = paymentApprovalRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment approval not found"));

            approval.setStatus(status == ApprovalStatus.APPROVED);
            approval.setApprovedAt(LocalDateTime.now());

            PaymentApproval updated = paymentApprovalRepository.save(approval);
            return new ApiResponse<>(true, "Payment approval updated successfully", PaymentApprovalDto.fromEntity(updated));
        } catch (ResponseStatusException e) {
            log.error("Approval not found for update", e);
            throw e;
        } catch (Exception e) {
            log.error("Error while updating approval status", e);
            return new ApiResponse<>(false, "Error while updating approval status", null);
        }
    }
}