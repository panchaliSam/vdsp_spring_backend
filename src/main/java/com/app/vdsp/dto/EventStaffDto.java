package com.app.vdsp.dto;

import com.app.vdsp.entity.EventStaff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventStaffDto {

    private Long id;
    private Long eventId;
    private Long staffId;
    private LocalDate eventDate;
    private LocalDateTime assignedAt;

    public static EventStaffDto fromEntity(EventStaff eventStaff) {
        return EventStaffDto.builder()
                .id(eventStaff.getId())
                .eventId(eventStaff.getEvent().getId())
                .staffId(eventStaff.getStaff() != null ? eventStaff.getStaff().getId() : null)
                .eventDate(eventStaff.getEventDate())
                .assignedAt(eventStaff.getAssignedAt())
                .build();
    }
}