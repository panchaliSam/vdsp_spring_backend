package com.app.vdsp.service;

import com.app.vdsp.dto.RoleDto;

import java.util.List;

public interface RoleService {
    RoleDto createRole(RoleDto roleDto, String authHeader);
    RoleDto updateRole(Long id, RoleDto roleDto, String authHeader);
    void deleteRole(Long id, String authHeader);
    RoleDto getRoleById(Long id);
    List<RoleDto> getAllRoles();
}