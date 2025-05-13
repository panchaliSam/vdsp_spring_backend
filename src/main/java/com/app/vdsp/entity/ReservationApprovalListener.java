package com.app.vdsp.entity;

import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class ReservationApprovalListener {

    @PreUpdate
    public void onPreUpdate(ReservationApproval reservationApproval) {
        if(Boolean.TRUE.equals(reservationApproval.getStatus()) && reservationApproval.getApprovedAt() == null) {
            reservationApproval.setApprovedAt(LocalDateTime.now());
        }
    }
}
