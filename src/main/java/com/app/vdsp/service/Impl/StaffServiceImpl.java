package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.StaffDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.repository.StaffRepository;
import com.app.vdsp.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    @Override
    public ApiResponse<List<StaffDto>> getAllStaff() {
        List<StaffDto> staffList = staffRepository.findAll().stream()
                .map(StaffDto::fromEntity)
                .collect(Collectors.toList());
        return new ApiResponse<>(
                true,
                "Fetched all staff records",
                staffList
        );
    }
}