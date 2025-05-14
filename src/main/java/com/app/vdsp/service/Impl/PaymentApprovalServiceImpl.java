package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.PaymentApprovalDto;
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
    public List<PaymentApprovalDto> getAllPaymentApprovals() {
        try {
            List<PaymentApproval> paymentApprovals = paymentApprovalRepository.findAll();
            return paymentApprovals.stream()
                    .map(PaymentApprovalDto::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while fetching approvals", e);
        }
    }

    @Override
    public List<PaymentApprovalDto> getApprovedPayments(String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is missing or invalid");
            }

            Long userId = jwtService.extractUserId(authorizationHeader.substring(7));
            List<PaymentApproval> approvals = paymentApprovalRepository.findByUserId(userId);

            return approvals.stream()
                    .map(PaymentApprovalDto::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get user payments", e);
        }
    }

    @Override
    public PaymentApprovalDto updateApprovalStatus(Long id, ApprovalStatus status) {
        PaymentApproval approval = paymentApprovalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment approval not found"));

        approval.setStatus(status == ApprovalStatus.APPROVED);
        approval.setApprovedAt(LocalDateTime.now());

        PaymentApproval updated = paymentApprovalRepository.save(approval);
        return PaymentApprovalDto.fromEntity(updated);
    }
}