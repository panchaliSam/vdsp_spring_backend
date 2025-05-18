package com.app.vdsp.service;

import java.util.Map;

public interface CalendarEventService {
    Map<String, Object> getCalendarEventDataWithHolidays(String authHeader);
}