package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.StaffDto;
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
    public List<StaffDto> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(StaffDto::fromEntity)
                .collect(Collectors.toList());
    }
}
