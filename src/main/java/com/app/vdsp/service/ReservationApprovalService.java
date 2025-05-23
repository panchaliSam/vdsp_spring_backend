package com.app.vdsp.service;

import com.app.vdsp.dto.ReservationApprovalDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.type.ApprovalStatus;

import java.util.List;

public interface ReservationApprovalService {
    ApiResponse<List<ReservationApprovalDto>> getAllReservationApprovals();
    ApiResponse<ReservationApprovalDto> updateApprovalStatus(Long id, ApprovalStatus status);
    ApiResponse<List<ReservationApprovalDto>> getApprovedReservations(String authorizationHeader);
}