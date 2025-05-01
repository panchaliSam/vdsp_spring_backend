package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.ReservationApprovalDto;
import com.app.vdsp.entity.ReservationApproval;
import com.app.vdsp.repository.ReservationApprovalRepository;
import com.app.vdsp.service.ReservationApprovalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationApprovalServiceImpl implements ReservationApprovalService {

    private static final Logger log = LoggerFactory.getLogger(ReservationApprovalServiceImpl.class);

    private final ReservationApprovalRepository reservationApprovalRepository;

    public ReservationApprovalServiceImpl(ReservationApprovalRepository reservationApprovalRepository) {
        this.reservationApprovalRepository = reservationApprovalRepository;
    }

    @Override
    public List<ReservationApprovalDto> getAllReservationApprovals() {
        try{
            List<ReservationApproval> reservationApprovals = reservationApprovalRepository.findAll();
            log.info("Reservation approvals found: {}", reservationApprovals);

            return reservationApprovals.stream()
                    .map(ReservationApprovalDto::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
                log.error("Error while getting reservation approvals", e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error while getting reservation approvals", e);
        }
    }
}
