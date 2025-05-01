package com.app.vdsp.service;

import com.app.vdsp.dto.ReservationApprovalDto;
import com.app.vdsp.type.ApprovalStatus;

import java.util.List;

public interface ReservationApprovalService {
    List<ReservationApprovalDto> getAllReservationApprovals();
    ReservationApprovalDto updateApprovalStatus(Long id, ApprovalStatus status);
}
