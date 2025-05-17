package com.app.vdsp.service;

import com.app.vdsp.dto.RoleDto;
import com.app.vdsp.entity.ApiResponse;

import java.util.List;

public interface RoleService {
    ApiResponse<RoleDto> createRole(RoleDto roleDto, String authHeader);
    ApiResponse<RoleDto> updateRole(Long id, RoleDto roleDto, String authHeader);
    ApiResponse<String> deleteRole(Long id, String authHeader);
    ApiResponse<RoleDto> getRoleById(Long id);
    ApiResponse<List<RoleDto>> getAllRoles();
}