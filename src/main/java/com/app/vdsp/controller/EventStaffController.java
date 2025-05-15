package com.app.vdsp.controller;

import com.app.vdsp.dto.EventStaffDto;
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
    public ResponseEntity<List<EventStaffDto>> getAll(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(eventStaffService.getAllEventStaff(authHeader));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EventStaffDto> getById(@PathVariable Long id,
                                                 @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(eventStaffService.getById(id, authHeader));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/assignByName")
    public ResponseEntity<EventStaffDto> assignStaffByName(@PathVariable Long id,
                                                           @RequestBody Map<String, String> body,
                                                           @RequestHeader("Authorization") String authHeader) {
        String staffName = body.get("staffName");
        return ResponseEntity.ok(eventStaffService.assignStaffByName(id, staffName, authHeader));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @RequestHeader("Authorization") String authHeader) {
        eventStaffService.delete(id, authHeader);
        return ResponseEntity.ok().build();
    }
}