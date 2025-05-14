package com.app.vdsp.entity;

import com.app.vdsp.type.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(name = "merchant_id", nullable = false, length = 50)
    private String merchantId;

    @Column(name = "payhere_payment_id", nullable = false, length = 50)
    private String paymentId;

    @Column(name = "payment_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal payhereAmount;

    @Column(name = "payment_currency", nullable = false, length = 10)
    private String payhereCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_code", nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    @Column(name = "md5_signature", nullable = false, length = 255)
    private String md5Signature;

    @Column(name = "custom_1", length = 255)
    private String custom1;

    @Column(name = "custom_2", length = 255)
    private String custom2;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Column(name = "status_message", length = 500)
    private String statusMessage;

    @Column(name = "card_holder_name", length = 255)
    private String cardHolderName;

    @Column(name = "card_no", length = 20)
    private String cardNo;

    @Column(name = "card_expiry", length = 10)
    private String cardExpiry;

    @Column(name = "created_at", nullable = false, updatable = false)
    @org.springframework.data.annotation.CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @org.springframework.data.annotation.LastModifiedDate
    private LocalDateTime updatedAt;
}