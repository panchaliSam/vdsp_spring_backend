package com.app.vdsp.dto;

import com.app.vdsp.type.PaymentStatus;
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
public class PaymentDto {

    private Long reservationId;

    private Long userId;

    private PaymentStatus paymentStatus;

    private String paymentUrl;

    private BigDecimal paymentAmount;

    private String paymentMethod;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
