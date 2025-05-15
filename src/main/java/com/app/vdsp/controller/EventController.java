package com.app.vdsp.controller;

import com.app.vdsp.dto.EventDto;
import com.app.vdsp.service.EventService;
import com.app.vdsp.type.AlbumStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @PatchMapping("/{id}/album-status")
    public ResponseEntity<EventDto> updateAlbumStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authHeader) {

        String statusStr = body.get("albumStatus");
        if (statusStr == null || statusStr.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        AlbumStatus status;
        try {
            status = AlbumStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }

        EventDto updated = eventService.updateAlbumStatus(id, status, authHeader);
        return ResponseEntity.ok(updated);
    }
}