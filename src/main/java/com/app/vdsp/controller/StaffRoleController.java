package com.app.vdsp.controller;

import com.app.vdsp.dto.StaffRoleDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.entity.StaffRole;
import com.app.vdsp.service.StaffRoleService;
import com.app.vdsp.type.StaffAssignStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staffRoles")
@RequiredArgsConstructor
public class StaffRoleController {

    private final StaffRoleService staffRoleService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<StaffRoleDto>>> getAll(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(staffRoleService.getAllStaffRoles(authHeader));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffRoleDto>> getById(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(staffRoleService.getStaffRoleById(id, authHeader));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffRoleDto>> update(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authHeader) {

        String statusStr = body.get("assignStatus");
        String roleName = body.get("roleName");

        StaffAssignStatus assignStatus = null;
        if (statusStr != null && !statusStr.isBlank()) {
            assignStatus = StaffAssignStatus.valueOf(statusStr.toUpperCase());
        }

        ApiResponse<StaffRoleDto> updated = staffRoleService.updateStaffRole(id, assignStatus, roleName, authHeader);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(staffRoleService.deleteStaffRole(id, authHeader));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<ApiResponse<List<StaffRole>>> getRolesByStaffId(
            @PathVariable Long staffId,
            @RequestHeader("Authorization") String authHeader) {

        return ResponseEntity.ok(staffRoleService.getRolesByStaffId(staffId, authHeader));
    }
}