package com.app.vdsp.dto;

import com.app.vdsp.entity.Reservation;
import com.app.vdsp.entity.ReservationApproval;
import com.app.vdsp.type.ApprovalStatus;
import com.app.vdsp.type.EventType;
import com.app.vdsp.type.SessionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationApprovalDto {

    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "Approval date cannot be null")
    private LocalDateTime approvedAt;

    @NotNull(message = "Status cannot be null")
    private ApprovalStatus status;

    @NotNull(message = "Customer name cannot be null")
    private String customerName;

    @NotNull(message = "Event type cannot be null")
    private EventType eventType;

    @NotNull(message = "Reservation ID cannot be null")
    private Long reservationId;

    @NotNull(message = "Package ID cannot be null")
    private Long packageId;

    @NotNull(message = "Event location cannot be null")
    private String eventLocation;

    @NotNull(message = "Event date cannot be null")
    private LocalDate eventDate;

    @NotNull(message = "Event start time cannot be null")
    private LocalTime eventStartTime;

    @NotNull(message = "Event end time cannot be null")
    private LocalTime eventEndTime;

    @NotNull(message = "Session type cannot be null")
    private SessionType sessionType;

    public static ReservationApprovalDto fromEntity(ReservationApproval reservationApproval) {
        Reservation reservation = reservationApproval.getReservation();
        ApprovalStatus approvalStatus = (reservationApproval.getStatus() == null || !reservationApproval.getStatus())
                ? ApprovalStatus.PENDING
                : (reservationApproval.getStatus() ? ApprovalStatus.APPROVED : ApprovalStatus.DISAPPROVED);

        return ReservationApprovalDto.builder()
                .id(reservationApproval.getId())
                .approvedAt(reservationApproval.getApprovedAt())
                .status(approvalStatus)
                .customerName(reservation.getUser().getFirstName() + " " + reservation.getUser().getLastName())
                .eventType(reservation.getEventType())
                .reservationId(reservation.getId()) // Include Reservation ID
                .packageId(reservation.getEventPackage().getId())
                .eventLocation(reservation.getEventLocation())
                .eventDate(reservation.getEventDate())
                .eventStartTime(reservation.getEventStartTime())
                .eventEndTime(reservation.getEventEndTime())
                .sessionType(reservation.getSessionType())
                .build();
    }
}
