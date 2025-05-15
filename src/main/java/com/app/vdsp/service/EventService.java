package com.app.vdsp.service;

import com.app.vdsp.dto.EventDto;
import com.app.vdsp.type.AlbumStatus;

public interface EventService {
    EventDto updateAlbumStatus(Long id, AlbumStatus albumStatus, String authHeader);
}
