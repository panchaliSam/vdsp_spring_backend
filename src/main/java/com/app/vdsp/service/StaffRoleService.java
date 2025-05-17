package com.app.vdsp.service;

import com.app.vdsp.dto.StaffRoleDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.entity.StaffRole;
import com.app.vdsp.type.StaffAssignStatus;

import java.util.List;

public interface StaffRoleService {
    ApiResponse<List<StaffRoleDto>> getAllStaffRoles(String authHeader);
    ApiResponse<StaffRoleDto> getStaffRoleById(Long id, String authHeader);
    ApiResponse<StaffRoleDto> updateStaffRole(Long id, StaffAssignStatus assignStatus, String roleName, String authHeader);
    ApiResponse<String> deleteStaffRole(Long id, String authHeader);
    ApiResponse<List<StaffRole>> getRolesByStaffId(Long staffId, String authHeader);
}