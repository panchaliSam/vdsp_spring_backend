package com.app.vdsp.dto;

import com.app.vdsp.type.EventType;
import jakarta.validation.constraints.NotBlank;
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
public class ReservationDto {

    @NotNull(message="User ID cannot be null")
    private Long userId;

    @NotNull(message = "Event type cannot be null")
    private EventType eventType;

    @NotNull(message = "Package ID cannot be null")
    private Long packageId;

    @NotBlank(message = "Event location cannot be blank")
    private String eventLocation;

    @NotNull(message = "Event date cannot be null")
    private LocalDate eventDate;

    @NotNull(message = "Event start time cannot be null")
    private LocalTime eventStartTime;

    @NotNull(message = "Event end time cannot be null")
    private LocalTime eventEndTime;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
