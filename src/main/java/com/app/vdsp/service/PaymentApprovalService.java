package com.app.vdsp.service;

import com.app.vdsp.dto.PaymentApprovalDto;
import com.app.vdsp.type.ApprovalStatus;

import java.util.List;

public interface PaymentApprovalService {
    List<PaymentApprovalDto> getAllPaymentApprovals();
    List<PaymentApprovalDto> getApprovedPayments(String authorizationHeader);
    PaymentApprovalDto updateApprovalStatus(Long id, ApprovalStatus status);
}