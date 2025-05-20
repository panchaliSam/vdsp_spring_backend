package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.EventDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.entity.Event;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.EventRepository;
import com.app.vdsp.service.EventService;
import com.app.vdsp.type.AlbumStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);
    private final EventRepository eventRepository;
    private final AuthorizationHelper  authorizationHelper;

    @Override
    public ApiResponse<EventDto> updateAlbumStatus(Long id, AlbumStatus albumStatus, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        event.setAlbumStatus(albumStatus);
        Event updated = eventRepository.save(event);

        log.info("Updated album status for event ID {}: {}", id, albumStatus);
        return new ApiResponse<>(true, "Album status updated successfully", EventDto.fromEntity(updated));
    }

    @Override
    public ApiResponse<List<EventDto>> getAllEvents(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        log.info("Fetching all events");
        List<EventDto> events = eventRepository.findAll()
                .stream()
                .map(EventDto::fromEntity)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "Fetched all events", events);
    }

    @Override
    public ApiResponse<List<EventDto>> getAllEventsForStatus(String authHeader, AlbumStatus status) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        log.info("Fetching all events for status: {}", status);
        List<EventDto> events = eventRepository.findAll()
                .stream()
                .filter(e -> e.getAlbumStatus() == status)
                .map(EventDto::fromEntity)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "Fetched all events", events);
    }

    @Override
    public ApiResponse<List<EventDto>> getAllEventsForCustomer(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        Long userId = authorizationHelper.extractUserId(authHeader);
        List<EventDto> data = eventRepository
                .findByReservation_UserId(userId)
                .stream()
                .map(EventDto::fromEntity)
                .collect(Collectors.toList());

        return new ApiResponse<>(true,
                "Fetched events for customer " + userId,
                data
        );
    }
}