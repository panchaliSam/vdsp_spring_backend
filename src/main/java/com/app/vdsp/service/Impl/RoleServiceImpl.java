package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.RoleDto;
import com.app.vdsp.entity.Role;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.RoleRepository;
import com.app.vdsp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleDto createRole(RoleDto roleDto, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        if (roleRepository.existsByRoleName(roleDto.getRoleName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Role already exists");
        }

        Role role = Role.builder()
                .roleName(roleDto.getRoleName())
                .build();

        Role saved = roleRepository.save(role);
        return toDto(saved);
    }

    @Override
    public RoleDto updateRole(Long id, RoleDto roleDto, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        existing.setRoleName(roleDto.getRoleName());
        Role updated = roleRepository.save(existing);
        return toDto(updated);
    }

    @Override
    public void deleteRole(Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        if (!roleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }

        roleRepository.deleteById(id);
    }

    @Override
    public RoleDto getRoleById(Long id) {
        return roleRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private RoleDto toDto(Role role) {
        return RoleDto.builder()
                .roleId(role.getRoleId())
                .roleName(role.getRoleName())
                .build();
    }
}
