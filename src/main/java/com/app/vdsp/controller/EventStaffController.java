package com.app.vdsp.controller;

import com.app.vdsp.dto.AssignMultipleStaffDto;
import com.app.vdsp.dto.EventStaffDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.service.EventStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/eventStaff")
@RequiredArgsConstructor
public class EventStaffController {

    private final EventStaffService eventStaffService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<EventStaffDto>>> getAll(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(eventStaffService.getAllEventStaff(authHeader));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventStaffDto>> getById(@PathVariable Long id,
                                                              @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(eventStaffService.getById(id, authHeader));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{eventStaffId}/assignByName")
    public ResponseEntity<ApiResponse<String>> assignMultipleStaff(
            @PathVariable Long eventStaffId,
            @RequestBody AssignMultipleStaffDto request,
            @RequestHeader("Authorization") String authHeader) {
        ApiResponse<String> response = eventStaffService.assignMultipleStaffByNames(eventStaffId, request, authHeader);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_STAFF')")
    @GetMapping("/my-events")
    public ResponseEntity<ApiResponse<List<EventStaffDto>>> getMyAssignedEvents(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(eventStaffService.getEventsForLoggedInStaff(authHeader));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id,
                                                      @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(eventStaffService.delete(id, authHeader));
    }
}