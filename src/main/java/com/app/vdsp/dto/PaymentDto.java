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

    private Long id;

    private Long reservationId;

    private String merchantId;

    private String paymentId;

    private BigDecimal payhereAmount;

    private String payhereCurrency;

    private PaymentStatus paymentStatus;

    private String md5Signature;

    private String custom1;

    private String custom2;

    private String paymentMethod;

    private String statusMessage;

    private String cardHolderName;

    private String cardNo;

    private String cardExpiry;

}
