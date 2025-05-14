package com.app.vdsp.controller;

import com.app.vdsp.dto.StaffRoleDto;
import com.app.vdsp.entity.StaffRole;
import com.app.vdsp.service.StaffRoleService;
import com.app.vdsp.type.StaffAssignStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staffRoles")
@RequiredArgsConstructor
public class StaffRoleController {

    @Autowired
    private final StaffRoleService staffRoleService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/getAll")
    public ResponseEntity<List<StaffRoleDto>> getAll(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(staffRoleService.getAllStaffRoles(authHeader));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<StaffRoleDto> getById(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(staffRoleService.getStaffRoleById(id, authHeader));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<StaffRoleDto> update(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authHeader) {

        String statusStr = body.get("assignStatus");
        String roleName = body.get("roleName");

        StaffAssignStatus assignStatus = null;
        if (statusStr != null && !statusStr.isBlank()) {
            assignStatus = StaffAssignStatus.valueOf(statusStr.toUpperCase());
        }

        StaffRoleDto updated = staffRoleService.updateStaffRole(id, assignStatus, roleName, authHeader);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        staffRoleService.deleteStaffRole(id, authHeader);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<StaffRole>> getRolesByStaffId(
            @PathVariable Long staffId,
            @RequestHeader("Authorization") String authHeader) {

        List<StaffRole> roles = staffRoleService.getRolesByStaffId(staffId, authHeader);
        return ResponseEntity.ok(roles);
    }
}
