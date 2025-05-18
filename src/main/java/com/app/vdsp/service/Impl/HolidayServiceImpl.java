package com.app.vdsp.service.impl;

import com.app.vdsp.dto.HolidayDto;
import com.app.vdsp.entity.Holiday;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.HolidayRepository;
import com.app.vdsp.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;

    @Override
    public HolidayDto addHoliday(HolidayDto dto, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        Holiday holiday = Holiday.builder()
                .name(dto.getName())
                .date(dto.getDate())
                .build();

        Holiday saved = holidayRepository.save(holiday);
        return HolidayDto.builder()
                .name(saved.getName())
                .date(saved.getDate())
                .build();
    }
}