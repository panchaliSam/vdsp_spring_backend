package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.EventStaffDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.entity.EventStaff;
import com.app.vdsp.entity.Staff;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.EventStaffRepository;
import com.app.vdsp.repository.StaffRepository;
import com.app.vdsp.service.EventStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventStaffServiceImpl implements EventStaffService {

    private final EventStaffRepository eventStaffRepository;
    private final StaffRepository staffRepository;
    private final AuthorizationHelper authorizationHelper;

    @Override
    public ApiResponse<List<EventStaffDto>> getAllEventStaff(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        List<EventStaffDto> data = eventStaffRepository.findAll()
                .stream()
                .map(EventStaffDto::fromEntity)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "Fetched all event staff records", data);
    }

    @Override
    public ApiResponse<EventStaffDto> getById(Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        EventStaff eventStaff = eventStaffRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "EventStaff not found"));
        return new ApiResponse<>(true, "EventStaff fetched successfully", EventStaffDto.fromEntity(eventStaff));
    }

    @Override
    public ApiResponse<EventStaffDto> assignStaffByName(Long eventStaffId, String staffFullName, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        String[] parts = staffFullName.trim().split(" ");
        if (parts.length < 2) {
            return new ApiResponse<>(false, "Full name must include first and last name", null);
        }

        String firstName = parts[0];
        String lastName = parts[1];

        Staff staff = staffRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff not found by name"));

        EventStaff eventStaff = eventStaffRepository.findById(eventStaffId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "EventStaff record not found"));

        boolean alreadyAssigned = eventStaffRepository.existsByStaffIdAndEventDate(staff.getId(), eventStaff.getEventDate());
        if (alreadyAssigned) {
            return new ApiResponse<>(false, "Staff already assigned for this date", null);
        }

        eventStaff.setStaff(staff);
        eventStaff.setAssignedAt(LocalDateTime.now());
        eventStaffRepository.save(eventStaff);

        return new ApiResponse<>(true, "Staff assigned successfully", EventStaffDto.fromEntity(eventStaff));
    }

    @Override
    public ApiResponse<String> delete(Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        eventStaffRepository.deleteById(id);
        return new ApiResponse<>(true, "EventStaff deleted successfully", null);
    }

    @Override
    public ApiResponse<List<EventStaffDto>> getEventsForLoggedInStaff(String authHeader) {
        authorizationHelper.ensureAuthorizationHeader(authHeader);
        Long userId = authorizationHelper.extractUserId(authHeader);

        Staff staff = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff profile not found for user"));

        List<EventStaffDto> data = eventStaffRepository.findByStaffId(staff.getId())
                .stream()
                .map(EventStaffDto::fromEntity)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Fetched events for logged-in staff", data);
    }
}