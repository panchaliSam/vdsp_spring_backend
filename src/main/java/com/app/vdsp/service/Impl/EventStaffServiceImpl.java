package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.AssignMultipleStaffDto;
import com.app.vdsp.dto.EventStaffDto;
import com.app.vdsp.entity.*;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.EventStaffRepository;
import com.app.vdsp.repository.NotificationRepository;
import com.app.vdsp.repository.StaffRepository;
import com.app.vdsp.service.EventStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventStaffServiceImpl implements EventStaffService {

    private final EventStaffRepository eventStaffRepository;
    private final StaffRepository staffRepository;
    private final AuthorizationHelper authorizationHelper;
    private final NotificationRepository notificationRepository;

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
    public ApiResponse<String> assignMultipleStaffByNames(
            Long  eventStaffSlotId,
            AssignMultipleStaffDto request,
            String authHeader
    ) {
        authorizationHelper.ensureAuthorizationHeader(authHeader);

        // look up the “base slot” just to get event & date
        EventStaff baseSlot = eventStaffRepository.findById(eventStaffSlotId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "EventStaff slot not found"));

        Event event       = baseSlot.getEvent();
        LocalDate date    = baseSlot.getEventDate();
        int assignedCount = 0;

        // grab any truly empty slots
        List<EventStaff> emptySlots =
                eventStaffRepository.findByEventIdAndEventDateAndStaffIsNull(
                        event.getId(), date);

        int slotIndex = 0;

        for (String fullName : request.getStaffNames()) {
            // parse “First Last”
            String[] parts = fullName.trim().split("\\s+");
            if (parts.length < 2) continue;

            String first = parts[0], last = parts[1];

            // find the staff entity
            Staff staff = staffRepository
                    .findByFirstNameIgnoreCaseAndLastNameIgnoreCase(first, last)
                    .orElse(null);
            if (staff == null) continue;

            // skip if already on that date
            if (eventStaffRepository.existsByStaffIdAndEventDate(
                    staff.getId(), date)) {
                continue;
            }

            EventStaff slot;
            if (slotIndex < emptySlots.size()) {
                // fill an existing empty one
                slot = emptySlots.get(slotIndex);
            } else {
                // **no empty slots left** — create a brand-new row
                slot = new EventStaff();
                slot.setEvent(event);
                slot.setEventDate(date);
            }

            slot.setStaff(staff);
            slot.setAssignedAt(LocalDateTime.now());
            eventStaffRepository.save(slot);

            assignedCount++;
            slotIndex++;
        }

        return new ApiResponse<>(true,
                assignedCount + " staff assigned successfully",
                null);
    }

    @Override
    public ApiResponse<String> unassignStaff(Long slotId, String authHeader) {
        authorizationHelper.ensureAuthorizationHeader(authHeader);

        EventStaff slot = eventStaffRepository.findById(slotId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Assignment slot not found"));

        // simply clear it
        slot.setStaff(null);
        slot.setAssignedAt(null);
        eventStaffRepository.save(slot);

        return new ApiResponse<>(true,
                "Staff unassigned successfully",
                null);
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