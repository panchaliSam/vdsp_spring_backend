package com.app.vdsp.service;

import com.app.vdsp.dto.StaffDto;
import com.app.vdsp.entity.ApiResponse;

import java.util.List;

public interface StaffService {
    ApiResponse<List<StaffDto>> getAllStaff();
}