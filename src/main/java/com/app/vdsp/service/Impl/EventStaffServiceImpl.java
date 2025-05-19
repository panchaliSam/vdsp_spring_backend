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
            Long eventStaffSlotId,
            AssignMultipleStaffDto request,
            String authHeader
    ) {
        authorizationHelper.ensureAuthorizationHeader(authHeader);

        EventStaff baseSlot = eventStaffRepository.findById(eventStaffSlotId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "EventStaff slot not found"
                ));

        Event    event        = baseSlot.getEvent();
        LocalDate date        = baseSlot.getEventDate();
        int       assignedCount = 0;

        List<EventStaff> emptySlots = eventStaffRepository
                .findByEventIdAndEventDateAndStaffIsNull(event.getId(), date);

        int slotIndex = 0;
        for (String fullName : request.getStaffNames()) {
            String[] parts = fullName.trim().split("\\s+");
            if (parts.length < 2) continue;

            String first = parts[0], last = parts[1];
            Staff  staff = staffRepository
                    .findByFirstNameIgnoreCaseAndLastNameIgnoreCase(first, last)
                    .orElse(null);
            if (staff == null) continue;

            // skip if already assigned that day
            if (eventStaffRepository.existsByStaffIdAndEventDate(staff.getId(), date))
                continue;

            EventStaff slot;
            if (slotIndex < emptySlots.size()) {
                slot = emptySlots.get(slotIndex);
            } else {
                slot = new EventStaff();
                slot.setEvent(event);
                slot.setEventDate(date);
            }
            slot.setStaff(staff);
            slot.setAssignedAt(LocalDateTime.now());
            eventStaffRepository.save(slot);
            assignedCount++;
            slotIndex++;

            // --- Send Assignment Notification ---
            Notification notif = Notification.builder()
                    .userId(staff.getUserId())              // or staff.getUser().getId()
                    .title("Assigned to “" + event.getId() + "”")
                    .message(String.format(
                            "You’ve been assigned to event '%s' on %s.",
                            event.getId(), date
                    ))
                    .createdAt(LocalDateTime.now())
                    .build();
            notificationRepository.save(notif);
        }

        return new ApiResponse<>(true,
                assignedCount + " staff assigned successfully",
                null
        );
    }

    @Override
    public ApiResponse<String> unassignStaff(Long slotId, String authHeader) {
        authorizationHelper.ensureAuthorizationHeader(authHeader);

        EventStaff slot = eventStaffRepository.findById(slotId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Assignment slot not found"
                ));

        Staff staff = slot.getStaff();
        LocalDate date = slot.getEventDate();
        Long eventName = slot.getEvent().getId();

        // clear assignment
        slot.setStaff(null);
        slot.setAssignedAt(null);
        eventStaffRepository.save(slot);

        // --- Send Removal Notification ---
        if (staff != null) {
            Notification notif = Notification.builder()
                    .userId(staff.getUserId())
                    .title("Removed from “" + eventName + "”")
                    .message(String.format(
                            "You’ve been removed from event '%s' on %s.",
                            eventName, date
                    ))
                    .createdAt(LocalDateTime.now())
                    .build();
            notificationRepository.save(notif);
        }

        return new ApiResponse<>(true,
                "Staff unassigned successfully",
                null
        );
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