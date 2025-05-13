package com.app.vdsp.service.Impl;

import com.app.vdsp.entity.Payment;
import com.app.vdsp.entity.Reservation;
import com.app.vdsp.repository.PaymentRepository;
import com.app.vdsp.repository.ReservationRepository;
import com.app.vdsp.service.PaymentService;
import com.app.vdsp.type.PaymentStatus;
import com.app.vdsp.utils.PayHereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PayHereService payHereService;
    private final ReservationRepository reservationRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, PayHereService payHereService, ReservationRepository reservationRepository) {
        this.paymentRepository = paymentRepository;
        this.payHereService = payHereService;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public String processPaymentNotification(Map<String, String> params) {
        String merchantId = params.get("merchant_id");
        String orderId = params.get("order_id");
        String payHereAmount = params.get("payhere_amount");
        String payHereCurrency = params.get("payhere_currency");
        int statusCode;
        String receivedMd5Sig = params.get("md5sig");

        try {
            statusCode = Integer.parseInt(params.get("status_code"));
        } catch (NumberFormatException e) {
            return "Invalid status code format.";
        }

        boolean isValid = payHereService.verifyPaymentStatus(
                merchantId,
                orderId,
                Double.parseDouble(payHereAmount),
                payHereCurrency,
                statusCode,
                receivedMd5Sig
        );

        if (!isValid) {
            System.err.println("Checksum verification failed for payment notification.");
            return "Invalid payment notification: checksum verification failed.";
        }

        // Retrieve the Reservation entity using the orderId
        Reservation reservation = reservationRepository.findById(Long.parseLong(orderId))
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID: " + orderId));

        Payment payment = new Payment();
        payment.setMerchantId(merchantId);
        payment.setPaymentId(params.get("payment_id"));
        payment.setReservation(reservation);
        payment.setPayhereAmount(new BigDecimal(payHereAmount));
        payment.setPayhereCurrency(payHereCurrency);
        payment.setPaymentStatus(PaymentStatus.fromStatusCode(statusCode));
        payment.setMd5Signature(receivedMd5Sig);
        payment.setCustom1(params.getOrDefault("custom_1", ""));
        payment.setCustom2(params.getOrDefault("custom_2", ""));
        payment.setPaymentMethod(params.get("method"));
        payment.setStatusMessage(params.getOrDefault("status_message", ""));
        payment.setCardHolderName(params.getOrDefault("card_holder_name", ""));
        payment.setCardNo(params.getOrDefault("card_no", ""));
        payment.setCardExpiry(params.getOrDefault("card_expiry", ""));

        paymentRepository.save(payment);

        return switch (statusCode) {
            case 2 -> "Payment successful.";
            case 0 -> "Payment pending.";
            case -1 -> "Payment canceled.";
            case -2 -> "Payment failed.";
            case -3 -> "Payment chargedback.";
            default -> "Unknown payment status.";
        };
    }


}
