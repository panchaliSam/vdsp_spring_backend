package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.ReservationApprovalDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.entity.Payment;
import com.app.vdsp.entity.ReservationApproval;
import com.app.vdsp.repository.PaymentApprovalRepository;
import com.app.vdsp.repository.PaymentRepository;
import com.app.vdsp.repository.ReservationApprovalRepository;
import com.app.vdsp.service.PaymentApprovalService;
import com.app.vdsp.service.ReservationApprovalService;
import com.app.vdsp.type.ApprovalStatus;
import com.app.vdsp.type.PaymentStatus;
import com.app.vdsp.utils.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationApprovalServiceImpl implements ReservationApprovalService {

    private static final Logger log = LoggerFactory.getLogger(ReservationApprovalServiceImpl.class);

    private final ReservationApprovalRepository reservationApprovalRepository;
    private final JWTService jwtService;
    private final PaymentRepository paymentRepository;

    public ReservationApprovalServiceImpl(ReservationApprovalRepository reservationApprovalRepository,
                                          JWTService jwtService,
                                          PaymentRepository paymentRepository) {
        this.reservationApprovalRepository = reservationApprovalRepository;
        this.jwtService = jwtService;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public ApiResponse<List<ReservationApprovalDto>> getAllReservationApprovals() {
        try {
            List<ReservationApproval> reservationApprovals = reservationApprovalRepository.findAll();
            log.info("Reservation approvals found: {}", reservationApprovals);

            List<ReservationApprovalDto> result = reservationApprovals.stream()
                    .map(reservationApproval -> {
                        Long rid = reservationApproval.getReservation().getId();
                        Payment p = paymentRepository.findPaymentByReservation_Id(rid);
                        if (p == null) {
                            return ReservationApprovalDto.fromEntity(reservationApproval, PaymentStatus.PENDING);
                        }else{
                            return ReservationApprovalDto.fromEntity(reservationApproval,p.getPaymentStatus());
                        }

                    })
                    .collect(Collectors.toList());

            return new ApiResponse<>(true, "Fetched all reservation approvals", result);
        } catch (Exception e) {
            log.error("Error while getting reservation approvals", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error while getting reservation approvals", e);
        }
    }

    @Override
    public ApiResponse<ReservationApprovalDto> updateApprovalStatus(Long id, ApprovalStatus status) {
        try {
            ReservationApproval reservationApproval = reservationApprovalRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation approval not found"));

            reservationApproval.setStatus(status == ApprovalStatus.APPROVED);
            reservationApproval.setApprovedAt(LocalDateTime.now());

            ReservationApproval updatedApproval = reservationApprovalRepository.save(reservationApproval);
            log.info("Reservation approval updated: {}", updatedApproval);


            return new ApiResponse<>(true, "Reservation approval status updated", ReservationApprovalDto.fromEntity(updatedApproval,null));
        } catch (Exception e) {
            log.error("Error while updating reservation approval status", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error while updating reservation approval status", e);
        }
    }

    @Override
    public ApiResponse<List<ReservationApprovalDto>> getApprovedReservations(String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is missing or invalid");
            }

            String token = authorizationHeader.substring(7);
            Long userId = jwtService.extractUserId(token);

            List<ReservationApproval> approvedReservations = reservationApprovalRepository.findByUserId(userId);
            log.info("Approved reservations for user {}: {}", userId, approvedReservations);

            List<ReservationApprovalDto> result = approvedReservations
                    .stream()
                    .map(reservationApproval -> {
                Long rid = reservationApproval.getReservation().getId();
                Payment p = paymentRepository.findPaymentByReservation_Id(rid);
                if (p == null) {
                    return ReservationApprovalDto.fromEntity(reservationApproval, PaymentStatus.PENDING);
                }else{
                    return ReservationApprovalDto.fromEntity(reservationApproval,p.getPaymentStatus());
                }

            }).collect(Collectors.toList());

            return new ApiResponse<>(true, "Fetched approved reservations for user", result);
        } catch (ResponseStatusException e) {
            log.error("Business error: {}", e.getReason(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while fetching approved reservations", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred while fetching approved reservations", e);
        }
    }
}