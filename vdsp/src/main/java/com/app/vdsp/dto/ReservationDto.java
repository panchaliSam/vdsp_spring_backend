package com.app.vdsp.dto;

import com.app.vdsp.entity.Reservation;
import com.app.vdsp.entity.ReservationPackage;
import com.app.vdsp.type.EventType;
import com.app.vdsp.type.SessionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDto {

    private String customerName;

    @NotNull(message = "Event type cannot be null")
    private EventType eventType;

    private Long packageId;

    @NotBlank(message = "Package name cannot be blank")
    private String packageName;

    private BigDecimal priceAmount;

    @NotBlank(message = "Event location cannot be blank")
    private String eventLocation;

    @NotNull(message = "Event date cannot be null")
    private LocalDate eventDate;

    @NotNull(message = "Event start time cannot be null")
    private LocalTime eventStartTime;

    @NotNull(message = "Event end time cannot be null")
    private LocalTime eventEndTime;

    @NotNull(message = "Session type cannot be null")
    private SessionType sessionType;

    public static ReservationDto fromEntity(Reservation reservation, ReservationPackage reservationPackage) {
        return ReservationDto.builder()
                .customerName(reservation.getUser().getFirstName() + " " + reservation.getUser().getLastName())
                .eventType(reservation.getEventType())
                .packageId(reservation.getEventPackage().getId())
                .packageName(reservationPackage.getName())
                .priceAmount(reservationPackage.getPrice())
                .eventLocation(reservation.getEventLocation())
                .eventDate(reservation.getEventDate())
                .eventStartTime(reservation.getEventStartTime())
                .eventEndTime(reservation.getEventEndTime())
                .sessionType(reservation.getSessionType())
                .build();
    }
}
