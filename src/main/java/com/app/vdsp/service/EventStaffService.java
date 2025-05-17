package com.app.vdsp.service;

import com.app.vdsp.dto.EventStaffDto;
import com.app.vdsp.entity.ApiResponse;

import java.util.List;

public interface EventStaffService {
    ApiResponse<List<EventStaffDto>> getAllEventStaff(String authHeader);
    ApiResponse<EventStaffDto> getById(Long id, String authHeader);
    ApiResponse<List<EventStaffDto>> getEventsForLoggedInStaff(String authHeader);
    ApiResponse<EventStaffDto> assignStaffByName(Long eventStaffId, String staffFullName, String authHeader);
    ApiResponse<String> delete(Long id, String authHeader);
}