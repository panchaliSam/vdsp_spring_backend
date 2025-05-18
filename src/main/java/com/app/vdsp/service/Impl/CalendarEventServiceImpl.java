package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.CalendarEventDto;
import com.app.vdsp.dto.HolidayDto;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.EventRepository;
import com.app.vdsp.repository.HolidayRepository;
import com.app.vdsp.repository.ReservationRepository;
import com.app.vdsp.service.CalendarEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarEventServiceImpl implements CalendarEventService {

    private final ReservationRepository reservationRepository;
    private final EventRepository eventRepository;
    private final HolidayRepository holidayRepository;

    @Override
    public Map<String, Object> getCalendarEventDataWithHolidays(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        List<CalendarEventDto> approved = eventRepository.findAll()
                .stream()
                .map(e -> CalendarEventDto.builder()
                        .eventDate(e.getEventDate())
                        .sessionType(e.getReservation().getSessionType())
                        .build())
                .collect(Collectors.toList());

        List<CalendarEventDto> pending = reservationRepository.findPendingReservations()
                .stream()
                .map(r -> CalendarEventDto.builder()
                        .eventDate(r.getEventDate())
                        .sessionType(r.getSessionType())
                        .build())
                .collect(Collectors.toList());

        List<HolidayDto> holidays = holidayRepository.findAll()
                .stream()
                .map(h -> HolidayDto.builder()
                        .name(h.getName())
                        .date(h.getDate())
                        .build())
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("approvedEventDatesWithSessionType", approved);
        result.put("pendingEventDatesWithSessionType", pending);
        result.put("holidays", holidays);

        return result;
    }
}