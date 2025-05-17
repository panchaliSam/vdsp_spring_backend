package com.app.vdsp.service.Impl;

import com.app.vdsp.entity.*;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.*;
import com.app.vdsp.service.EmailService;
import com.app.vdsp.service.PaymentService;
import com.app.vdsp.type.AlbumStatus;
import com.app.vdsp.type.PaymentStatus;
import com.app.vdsp.utils.PayHereService;
import com.app.vdsp.utils.PdfGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PayHereService payHereService;
    private final ReservationRepository reservationRepository;
    private final PaymentApprovalRepository paymentApprovalRepository;
    private final NotificationRepository notificationRepository;
    private final EventRepository eventRepository;
    private final EventStaffRepository eventStaffRepository;
    private final PdfGeneratorUtil pdfGeneratorUtil;
    private final EmailService emailService;
    private final ReservationDocumentRepository reservationDocumentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              PayHereService payHereService,
                              ReservationRepository reservationRepository,
                              PaymentApprovalRepository paymentApprovalRepository,
                              NotificationRepository notificationRepository,
                              EventRepository eventRepository,
                              EventStaffRepository eventStaffRepository,
                              PdfGeneratorUtil pdfGeneratorUtil,
                              EmailService emailService,
                              ReservationDocumentRepository reservationDocumentRepository) {
        this.paymentRepository = paymentRepository;
        this.payHereService = payHereService;
        this.reservationRepository = reservationRepository;
        this.paymentApprovalRepository = paymentApprovalRepository;
        this.notificationRepository = notificationRepository;
        this.eventRepository = eventRepository;
        this.eventStaffRepository = eventStaffRepository;
        this.pdfGeneratorUtil = pdfGeneratorUtil;
        this.emailService = emailService;
        this.reservationDocumentRepository = reservationDocumentRepository;
    }

    @Override
    public ApiResponse<String> processPaymentNotification(Map<String, String> params) {
        try {
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
                return new ApiResponse<>(false, "Invalid status_code format", null);
            }

            boolean isValid = payHereService.verifyPaymentStatus(
                    merchantId, orderId,
                    Double.parseDouble(payHereAmount), payHereCurrency,
                    statusCode, receivedMd5Sig
            );

            if (!isValid) {
                return new ApiResponse<>(false, "Signature verification failed", null);
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

            if (statusCode == 2) {
                User user = reservation.getUser();

                PaymentApproval approval = PaymentApproval.builder()
                        .payment(payment)
                        .user(user)
                        .status(true)
                        .approvedAt(null)
                        .build();
                paymentApprovalRepository.save(approval);

                Notification notification = Notification.builder()
                        .userId(user.getId())
                        .title("Payment Received")
                        .message("Your reservation has been approved after successful payment.")
                        .build();
                notificationRepository.save(notification);

                Event event = Event.builder()
                        .reservation(reservation)
                        .eventDate(reservation.getEventDate())
                        .albumStatus(AlbumStatus.IN_PROGRESS)
                        .build();
                eventRepository.save(event);

                EventStaff eventStaff = EventStaff.builder()
                        .event(event)
                        .staff(null)
                        .eventDate(reservation.getEventDate())
                        .assignedAt(null)
                        .build();
                eventStaffRepository.save(eventStaff);

                byte[] pdfBytes = pdfGeneratorUtil.generateReservationConfirmationPdf(reservation);

                ReservationDocument confirmationDoc = ReservationDocument.builder()
                        .reservation(reservation)
                        .name("Reservation Confirmation Letter")
                        .mimeType("application/pdf")
                        .data(pdfBytes)
                        .createdAt(LocalDateTime.now())
                        .build();
                reservationDocumentRepository.save(confirmationDoc);

                emailService.sendEmailWithAttachment(
                        user.getEmail(),
                        "Reservation Confirmation",
                        "Your reservation has been confirmed. Please find the confirmation letter attached.",
                        pdfBytes,
                        "Reservation_Confirmation_" + reservation.getId() + ".pdf"
                );
            }

            return new ApiResponse<>(true, "Payment processed: " + PaymentStatus.fromStatusCode(statusCode).name(), null);

        } catch (Exception ex) {
            ex.printStackTrace();
            return new ApiResponse<>(false, "Error occurred: " + ex.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<Boolean> isAlreadyPaid(Long reservationId, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        boolean paid = paymentRepository.existsByReservationIdAndPaymentStatus(reservationId, PaymentStatus.SUCCESS);
        return new ApiResponse<>(true, "Payment status checked", paid);
    }
}