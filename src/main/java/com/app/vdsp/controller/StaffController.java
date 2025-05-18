// StaffController.java
package com.app.vdsp.controller;

import com.app.vdsp.dto.StaffDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<StaffDto>>> getAllStaff(
            @RequestHeader("Authorization") String authHeader
    ) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        ApiResponse<List<StaffDto>> response = staffService.getAllStaff();
        return ResponseEntity.ok(response);
    }
}