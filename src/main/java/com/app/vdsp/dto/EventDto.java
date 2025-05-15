package com.app.vdsp.dto;

import com.app.vdsp.entity.Event;
import com.app.vdsp.type.AlbumStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
    private Long id;
    private Long reservationId;
    private LocalDate eventDate;
    private AlbumStatus albumStatus;

    public static EventDto fromEntity(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .reservationId(event.getReservation().getId())
                .eventDate(event.getEventDate())
                .albumStatus(event.getAlbumStatus())
                .build();
    }
}