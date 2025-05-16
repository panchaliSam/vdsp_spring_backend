package com.app.vdsp.service;

import com.app.vdsp.dto.EventDto;
import com.app.vdsp.type.AlbumStatus;

import java.util.List;

public interface EventService {
    EventDto updateAlbumStatus(Long id, AlbumStatus albumStatus, String authHeader);
    List<EventDto> getAllEvents(String authHeader);
}
