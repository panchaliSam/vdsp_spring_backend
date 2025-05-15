package com.app.vdsp.service;

import com.app.vdsp.dto.EventStaffDto;

import java.util.List;

public interface EventStaffService {
    List<EventStaffDto> getAllEventStaff(String authHeader);
    EventStaffDto getById(Long id, String authHeader);
    EventStaffDto assignStaffByName(Long eventStaffId, String staffFullName, String authHeader);
    void delete(Long id, String authHeader);
}