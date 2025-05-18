package com.app.vdsp.service;

import com.app.vdsp.dto.HolidayDto;

public interface HolidayService {
    HolidayDto addHoliday(HolidayDto dto, String authHeader);
}