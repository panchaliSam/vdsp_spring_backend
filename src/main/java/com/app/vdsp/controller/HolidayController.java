package com.app.vdsp.controller;

import com.app.vdsp.dto.HolidayDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService holidayService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<HolidayDto>> addHoliday(
            @RequestBody HolidayDto dto,
            @RequestHeader("Authorization") String authHeader
    ) {
        HolidayDto saved = holidayService.addHoliday(dto, authHeader);
        return ResponseEntity.ok(new ApiResponse<>(true, "Holiday added successfully", saved));
    }
}