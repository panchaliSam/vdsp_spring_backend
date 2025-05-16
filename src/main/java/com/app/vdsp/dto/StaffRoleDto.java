package com.app.vdsp.dto;

import com.app.vdsp.entity.Staff;
import com.app.vdsp.entity.StaffRole;
import com.app.vdsp.type.StaffAssignStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffRoleDto {
    private Long id;
    private StaffDto staff;
    private String roleName;
    private StaffAssignStatus assignStatus;
    private LocalDateTime assignedAt;

    public static StaffRoleDto fromEntity(StaffRole staffRole) {
        return StaffRoleDto.builder()
                .id(staffRole.getId())
                .staff(StaffDto.fromEntity(staffRole.getStaff()))
                .roleName(staffRole.getRole() != null ? staffRole.getRole().getRoleName() : null)
                .assignStatus(staffRole.getAssignStatus())
                .assignedAt(staffRole.getAssignedAt())
                .build();
    }
}