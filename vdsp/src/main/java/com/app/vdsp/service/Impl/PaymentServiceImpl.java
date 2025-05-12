package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.PaymentDto;
import com.app.vdsp.service.PaymentService;
import com.app.vdsp.type.PaymentStatus;
import com.app.vdsp.utils.PayHereService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PayHereService payHereService;

    public PaymentServiceImpl(PayHereService payHereService) {
        this.payHereService = payHereService;
    }

    @Override
    public String createPayment(String orderId, double amount, Model model) {
        String currency = "LKR";

        String hash = payHereService.generateHash(orderId, amount, currency);

        model.addAttribute("merchantId", payHereService.merchantId());
        model.addAttribute("sandboxUrl", payHereService.getSandboxUrl());
        model.addAttribute("returnUrl", payHereService.getReturnUrl());
        model.addAttribute("cancelUrl", payHereService.getCancelUrl());
        model.addAttribute("notifyUrl", payHereService.getNotifyUrl());
        model.addAttribute("orderId", orderId);
        model.addAttribute("items", "Order-" + orderId);
        model.addAttribute("currency", currency);
        model.addAttribute("amount", amount);
        model.addAttribute("firstName", "CustomerFirstName");
        model.addAttribute("lastName", "CustomerLastName");
        model.addAttribute("email", "customer@example.com");
        model.addAttribute("phone", "0712345678");
        model.addAttribute("address", "Customer Address");
        model.addAttribute("city", "Customer City");
        model.addAttribute("country", "Sri Lanka");
        model.addAttribute("hash", hash);

        return "paymentForm";
    }

    @Override
    public void updatePaymentStatus(Long paymentId, PaymentStatus status) {

    }
}
