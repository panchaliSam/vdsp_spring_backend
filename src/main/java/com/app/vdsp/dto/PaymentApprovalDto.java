package com.app.vdsp.dto;

import com.app.vdsp.entity.Payment;
import com.app.vdsp.entity.PaymentApproval;
import com.app.vdsp.type.ApprovalStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentApprovalDto {

    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "Approval date cannot be null")
    private LocalDateTime approvedAt;

    @NotNull(message = "Status cannot be null")
    private ApprovalStatus status;

    @NotNull(message = "Customer name cannot be null")
    private String customerName;

    @NotNull(message = "Reservation ID cannot be null")
    private Long reservationId;

    @NotNull(message = "Payment ID cannot be null")
    private Long paymentId;

    @NotNull(message = "Payment amount cannot be null")
    private BigDecimal amount;

    @NotNull(message = "Currency cannot be null")
    private String currency;

    @NotNull(message = "Payment method cannot be null")
    private String method;

    @NotNull(message = "Payment status message cannot be null")
    private String statusMessage;

    public static PaymentApprovalDto fromEntity(PaymentApproval paymentApproval) {
        Payment payment = paymentApproval.getPayment();
        ApprovalStatus approvalStatus = (paymentApproval.getStatus() == null || !paymentApproval.getStatus())
                ? ApprovalStatus.PENDING
                : (paymentApproval.getStatus() ? ApprovalStatus.APPROVED : ApprovalStatus.DISAPPROVED);

        return PaymentApprovalDto.builder()
                .id(paymentApproval.getId())
                .approvedAt(paymentApproval.getApprovedAt())
                .status(approvalStatus)
                .customerName(payment.getReservation().getUser().getFirstName() + " " + payment.getReservation().getUser().getLastName())
                .reservationId(payment.getReservation().getId())
                .paymentId(payment.getId())
                .amount(payment.getPayhereAmount())
                .currency(payment.getPayhereCurrency())
                .method(payment.getPaymentMethod())
                .statusMessage(payment.getStatusMessage())
                .build();
    }
}