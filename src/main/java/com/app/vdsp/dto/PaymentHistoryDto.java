package com.app.vdsp.dto;

import com.app.vdsp.entity.Payment;
import com.app.vdsp.entity.Reservation;
import com.app.vdsp.type.EventType;
import com.app.vdsp.type.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentHistoryDto {

    private Long paymentId;
    private String cardNo;
    private String cardExpiry;
    private String paymentMethod;
    private String currency;
    private BigDecimal amount;
    private PaymentStatus status;
    private Long reservationId;
    private EventType eventType;
    private LocalDate eventDate;

    public static PaymentHistoryDto fromEntity(Payment payment) {
        Reservation reservation = payment.getReservation();
        return PaymentHistoryDto.builder()
                .paymentId(payment.getId())
                .cardNo(payment.getCardNo())
                .cardExpiry(payment.getCardExpiry())
                .paymentMethod(payment.getPaymentMethod())
                .currency(payment.getPayhereCurrency())
                .amount(payment.getPayhereAmount())
                .status(payment.getPaymentStatus())
                .reservationId(reservation.getId())
                .eventType(reservation.getEventType())
                .eventDate(reservation.getEventDate())
                .build();
    }
}
