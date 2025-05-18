package com.app.vdsp.dto;

import com.app.vdsp.type.SessionType;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarEventDto {
    private LocalDate eventDate;
    private SessionType sessionType;
}