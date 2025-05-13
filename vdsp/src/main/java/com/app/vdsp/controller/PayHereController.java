package com.app.vdsp.controller;

import com.app.vdsp.dto.PaymentDto;
import com.app.vdsp.dto.PaymentRequestDto;
import com.app.vdsp.entity.Payment;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.service.PaymentService;
import com.app.vdsp.type.PaymentStatus;
import com.app.vdsp.utils.PayHereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PayHereController {

    private final PayHereService payHereService;
    private final PaymentService paymentService;

    @Autowired
    public PayHereController(PayHereService payHereService, PaymentService paymentService) {
        this.payHereService = payHereService;
        this.paymentService = paymentService;
    }

    @PostMapping("/generate-hash")
    public String generateHash(@RequestHeader("Authorization") String authHeader, @RequestBody PaymentRequestDto paymentRequest) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        String orderId = paymentRequest.getOrderId();
        double amount = paymentRequest.getAmount();
        String currency = "LKR";

        return payHereService.generateHash(orderId, amount, currency);
    }


    @PostMapping("/notify")
    public String notify(@RequestParam Map<String, String> params) {
        // Map the POST params to the PaymentDto
        Payment payment = new Payment();
        payment.setMerchantId(params.get("merchant_id"));
        payment.setPaymentId(params.get("payment_id"));
        payment.setPayhereAmount(new BigDecimal(params.get("payhere_amount")));
        payment.setPayhereCurrency(params.get("payhere_currency"));
        payment.setPaymentStatus(PaymentStatus.valueOf(params.get("status_code"))); // Map status correctly
        payment.setMd5Signature(params.get("md5sig"));
        payment.setCustom1(params.get("custom_1"));
        payment.setCustom2(params.get("custom_2"));
        payment.setPaymentMethod(params.get("method"));
        payment.setStatusMessage(params.get("status_message"));
        payment.setCardHolderName(params.get("card_holder_name"));
        payment.setCardNo(params.get("card_no"));
        payment.setCardExpiry(params.get("card_expiry"));

        // Call the service to process the payment notification and save the payment details
        return paymentService.processPaymentNotification(payment);
    }
}
