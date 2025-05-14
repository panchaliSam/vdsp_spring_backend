package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.StaffRoleDto;
import com.app.vdsp.entity.Role;
import com.app.vdsp.entity.StaffRole;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.RoleRepository;
import com.app.vdsp.repository.StaffRoleRepository;
import com.app.vdsp.service.StaffRoleService;
import com.app.vdsp.type.StaffAssignStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffRoleServiceImpl implements StaffRoleService {

    private final StaffRoleRepository staffRoleRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<StaffRoleDto> getAllStaffRoles(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        return staffRoleRepository.findAll()
                .stream()
                .map(StaffRoleDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public StaffRoleDto getStaffRoleById(Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        StaffRole staffRole = staffRoleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "StaffRole not found"));
        return StaffRoleDto.fromEntity(staffRole);
    }

    @Override
    public StaffRoleDto updateStaffRole(Long id, StaffAssignStatus assignStatus, String roleName, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        StaffRole staffRole = staffRoleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "StaffRole not found"));

        // Update assign status
        staffRole.setAssignStatus(assignStatus);

        // Explicitly handle assignedAt
        if (assignStatus == StaffAssignStatus.ASSIGNED && staffRole.getAssignedAt() == null) {
            staffRole.setAssignedAt(java.time.LocalDateTime.now());
        }

        // Update role if provided
        if (roleName != null && !roleName.isBlank()) {
            Role role = roleRepository.findByRoleName(roleName.toUpperCase())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
            staffRole.setRole(role);
        }

        staffRoleRepository.save(staffRole);
        return StaffRoleDto.fromEntity(staffRole);
    }

    @Override
    public void deleteStaffRole(Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        staffRoleRepository.deleteById(id);
    }

    @Override
    public List<StaffRole> getRolesByStaffId(Long staffId, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        return staffRoleRepository.findAll()
                .stream()
                .filter(role -> role.getStaff().getId().equals(staffId))
                .toList();
    }


}