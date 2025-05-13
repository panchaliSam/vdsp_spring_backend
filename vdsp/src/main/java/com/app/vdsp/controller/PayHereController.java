package com.app.vdsp.controller;

import com.app.vdsp.dto.PaymentRequestDto;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.service.PaymentService;
import com.app.vdsp.utils.PayHereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return paymentService.processPaymentNotification(params);
    }
}
