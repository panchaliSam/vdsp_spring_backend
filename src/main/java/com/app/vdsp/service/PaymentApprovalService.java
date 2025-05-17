package com.app.vdsp.service;

import com.app.vdsp.dto.PaymentApprovalDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.type.ApprovalStatus;

import java.util.List;

public interface PaymentApprovalService {
    ApiResponse<List<PaymentApprovalDto>> getAllPaymentApprovals();
    ApiResponse<List<PaymentApprovalDto>> getApprovedPayments(String authorizationHeader);
    ApiResponse<PaymentApprovalDto> updateApprovalStatus(Long id, ApprovalStatus status);
}