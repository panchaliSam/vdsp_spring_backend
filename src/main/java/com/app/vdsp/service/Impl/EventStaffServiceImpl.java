package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.EventStaffDto;
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


    @Override
    public List<EventStaffDto> getAllEventStaff(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        return eventStaffRepository.findAll()
                .stream()
                .map(EventStaffDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public EventStaffDto getById(Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        EventStaff eventStaff = eventStaffRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "EventStaff not found"));
        return EventStaffDto.fromEntity(eventStaff);
    }

    @Override
    public EventStaffDto assignStaffByName(Long eventStaffId, String staffFullName, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        // Split name
        String[] parts = staffFullName.trim().split(" ");
        if (parts.length < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name must include first and last name");
        }

        String firstName = parts[0];
        String lastName = parts[1];

        Staff staff = staffRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff not found by name"));

        EventStaff eventStaff = eventStaffRepository.findById(eventStaffId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "EventStaff record not found"));

        // Check if this staff is already assigned for the date
        boolean alreadyAssigned = eventStaffRepository.existsByStaffIdAndEventDate(staff.getId(), eventStaff.getEventDate());
        if (alreadyAssigned) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Staff already assigned for this date");
        }

        eventStaff.setStaff(staff);
        eventStaff.setAssignedAt(LocalDateTime.now());
        eventStaffRepository.save(eventStaff);

        return EventStaffDto.fromEntity(eventStaff);
    }

    @Override
    public void delete(Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        eventStaffRepository.deleteById(id);
    }

    @Override
    public List<EventStaffDto> getEventsForLoggedInStaff(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        Long userId = AuthorizationHelper.extractUserId(authHeader);

        Staff staff = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff profile not found for user"));

        List<EventStaff> assignments = eventStaffRepository.findByStaffId(staff.getId());

        return assignments.stream()
                .map(EventStaffDto::fromEntity)
                .collect(Collectors.toList());
    }
}
