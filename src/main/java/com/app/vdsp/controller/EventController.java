package com.app.vdsp.controller;

import com.app.vdsp.dto.EventDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.service.EventService;
import com.app.vdsp.type.AlbumStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @PatchMapping("/{id}/album-status")
    public ResponseEntity<ApiResponse<EventDto>> updateAlbumStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authHeader) {

        String statusStr = body.get("albumStatus");
        if (statusStr == null || statusStr.isBlank()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "AlbumStatus is required", null));
        }

        AlbumStatus status;
        try {
            status = AlbumStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Invalid AlbumStatus", null));
        }

        return ResponseEntity.ok(eventService.updateAlbumStatus(id, status, authHeader));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<EventDto>>> getAllEvents(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(eventService.getAllEvents(authHeader));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/getAll/{status}")
    public ResponseEntity<ApiResponse<List<EventDto>>> getAllEventsForStatus(
            @PathVariable String status,
            @RequestHeader("Authorization") String authHeader) {
        AlbumStatus albumStatus = AlbumStatus.valueOf(status.toUpperCase());
        return ResponseEntity.ok(eventService.getAllEventsForStatus(authHeader,albumStatus));
    }

    @GetMapping("/my")
    public ApiResponse<List<EventDto>> getMyEvents(
            @RequestHeader("Authorization") String authHeader
    ) {
        return eventService.getAllEventsForCustomer(authHeader);
    }
}