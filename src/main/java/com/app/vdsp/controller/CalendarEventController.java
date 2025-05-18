package com.app.vdsp.controller;

import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.service.CalendarEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarEventController {

    private final CalendarEventService calendarEventService;

    @GetMapping("/events")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCalendarEvents(
            @RequestHeader("Authorization") String authHeader
    ) {
        Map<String, Object> data = calendarEventService.getCalendarEventDataWithHolidays(authHeader);
        return ResponseEntity.ok(new ApiResponse<>(true, "Fetched calendar event data", data));
    }
}