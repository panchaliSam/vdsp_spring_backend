package com.app.vdsp.controller;

import com.app.vdsp.dto.PaymentRequestDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.entity.ReservationDocument;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.ReservationDocumentRepository;
import com.app.vdsp.service.PaymentService;
import com.app.vdsp.utils.PayHereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PayHereController {

    private final PayHereService payHereService;
    private final PaymentService paymentService;
    private final ReservationDocumentRepository reservationDocumentRepository;

    @Value("${external.base.url}")
    private String externalBaseUrl;

    @Autowired
    public PayHereController(PayHereService payHereService,
                             PaymentService paymentService,
                             ReservationDocumentRepository reservationDocumentRepository) {
        this.payHereService = payHereService;
        this.paymentService = paymentService;
        this.reservationDocumentRepository = reservationDocumentRepository;
    }

    @PostMapping("/generate-hash")
    public ApiResponse<String> generateHash(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody PaymentRequestDto paymentRequest) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        String currency = "LKR";
        return payHereService.generateHash(paymentRequest.getOrderId(), paymentRequest.getAmount(), currency);
    }

    @PostMapping("/notify")
    public ResponseEntity<ApiResponse<String>> notify(@RequestParam Map<String, String> params) {
        System.out.println("Notify endpoint hit via: " + externalBaseUrl + "/api/payment/notify");
        return ResponseEntity.ok(paymentService.processPaymentNotification(params));
    }

    @GetMapping("/already-paid/{reservationId}")
    public ResponseEntity<ApiResponse<Boolean>> isAlreadyPaid(
            @PathVariable Long reservationId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        return ResponseEntity.ok(paymentService.isAlreadyPaid(reservationId, authHeader));
    }

    @GetMapping("/reservation/{id}/confirmation-letter")
    public ResponseEntity<ApiResponse<byte[]>> downloadConfirmationLetter(@PathVariable Long id) {
        ReservationDocument document = reservationDocumentRepository
                .findByReservationId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
        byte[] pdf = document.getData();
        ApiResponse<byte[]> envelope =
                new ApiResponse<>(true, "Fetched confirmation letter", pdf);
        return ResponseEntity.ok(envelope);
    }
}