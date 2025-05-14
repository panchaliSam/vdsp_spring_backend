package com.app.vdsp.controller;

import com.app.vdsp.dto.RoleDto;
import com.app.vdsp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto, @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(roleService.createRole(roleDto, authHeader));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable Long id, @RequestBody RoleDto roleDto, @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(roleService.updateRole(id, roleDto, authHeader));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        roleService.deleteRole(id, authHeader);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
}
