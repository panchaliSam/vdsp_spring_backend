package com.app.vdsp.service.Impl;

import com.app.vdsp.entity.Payment;
import com.app.vdsp.entity.PaymentApproval;
import com.app.vdsp.entity.Reservation;
import com.app.vdsp.entity.User;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.PaymentApprovalRepository;
import com.app.vdsp.repository.PaymentRepository;
import com.app.vdsp.repository.ReservationRepository;
import com.app.vdsp.repository.UserRepository;
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
    private final PaymentApprovalRepository paymentApprovalRepository;
    private final UserRepository userRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, PayHereService payHereService, ReservationRepository reservationRepository, PaymentApprovalRepository paymentApprovalRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.payHereService = payHereService;
        this.reservationRepository = reservationRepository;
        this.paymentApprovalRepository = paymentApprovalRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String processPaymentNotification(Map<String, String> params) {
        try {
            System.out.println(" PayHere Notification Received: " + params);

            String merchantId = params.get("merchant_id");
            String orderId = params.get("order_id");
            String paymentId = params.get("payment_id");
            String payHereAmount = params.get("payhere_amount");
            String payHereCurrency = params.get("payhere_currency");
            String receivedMd5Sig = params.get("md5sig");
            String statusCodeStr = params.get("status_code");

            int statusCode;
            try {
                statusCode = Integer.parseInt(statusCodeStr);
            } catch (NumberFormatException e) {
                System.err.println("Invalid status_code format: " + statusCodeStr);
                return "Invalid status_code format.";
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
                System.err.println("Signature verification failed.");
                return "Invalid signature.";
            }

            Reservation reservation = reservationRepository.findById(Long.parseLong(orderId))
                    .orElseThrow(() -> new IllegalArgumentException("Reservation not found for ID: " + orderId));

            Payment payment = Payment.builder()
                    .merchantId(merchantId)
                    .paymentId(paymentId)
                    .reservation(reservation)
                    .payhereAmount(new BigDecimal(payHereAmount))
                    .payhereCurrency(payHereCurrency)
                    .paymentStatus(PaymentStatus.fromStatusCode(statusCode))
                    .md5Signature(receivedMd5Sig)
                    .custom1(params.getOrDefault("custom_1", null))
                    .custom2(params.getOrDefault("custom_2", null))
                    .paymentMethod(params.getOrDefault("method", null))
                    .statusMessage(params.getOrDefault("status_message", null))
                    .cardHolderName(params.getOrDefault("card_holder_name", null))
                    .cardNo(params.getOrDefault("card_no", null))
                    .cardExpiry(params.getOrDefault("card_expiry", null))
                    .build();

            paymentRepository.save(payment);
            System.out.println("Payment saved for order ID: " + orderId);

            if (statusCode == 2) {
                User user = reservation.getUser();

                PaymentApproval approval = PaymentApproval.builder()
                        .payment(payment)
                        .user(user)
                        .status(false)
                        .approvedAt(null)
                        .build();

                paymentApprovalRepository.save(approval);
                System.out.println("PaymentApproval created for user ID: " + user.getId());
            }

            return switch (statusCode) {
                case 2 -> "Payment successful.";
                case 0 -> "Payment pending.";
                case -1 -> "Payment canceled.";
                case -2 -> "Payment failed.";
                case -3 -> "Payment chargedback.";
                default -> "Unknown payment status.";
            };

        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error occurred while processing payment: " + ex.getMessage();
        }
    }

    @Override
    public boolean isAlreadyPaid(Long reservationId, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        return paymentRepository.existsByReservationIdAndPaymentStatus(reservationId, PaymentStatus.SUCCESS);
    }

}
