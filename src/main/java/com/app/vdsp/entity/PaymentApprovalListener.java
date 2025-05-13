package com.app.vdsp.entity;

import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class PaymentApprovalListener {

    @PreUpdate
    public void onPreUpdate(PaymentApproval paymentApproval) {
        if (Boolean.TRUE.equals(paymentApproval.getStatus()) && paymentApproval.getApprovedAt() == null) {
            paymentApproval.setApprovedAt(LocalDateTime.now());
        }
    }
}
