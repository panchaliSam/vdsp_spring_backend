package com.app.vdsp.service;

import com.app.vdsp.dto.StaffRoleDto;
import com.app.vdsp.entity.StaffRole;
import com.app.vdsp.type.StaffAssignStatus;

import java.util.List;

public interface StaffRoleService {
    List<StaffRoleDto> getAllStaffRoles(String authHeader);
    StaffRoleDto getStaffRoleById(Long id, String authHeader);
    StaffRoleDto updateStaffRole(Long id, StaffAssignStatus assignStatus, String roleName, String authHeader);
    void deleteStaffRole(Long id, String authHeader);
    List<StaffRole> getRolesByStaffId(Long staffId, String authHeader);
}
