package com.app.vdsp.entity;

import com.app.vdsp.type.StaffAssignStatus;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class StaffRoleListener {

    @PrePersist
    public void onPrePersist(StaffRole staffRole) {
        if (staffRole.getAssignStatus() == null) {
            staffRole.setAssignStatus(StaffAssignStatus.NOT_ASSIGNED);
        }

        if (staffRole.getAssignStatus() == StaffAssignStatus.ASSIGNED && staffRole.getAssignedAt() == null) {
            staffRole.setAssignedAt(LocalDateTime.now());
        }
    }

    @PreUpdate
    public void onPreUpdate(StaffRole staffRole) {
        if (staffRole.getAssignStatus() == StaffAssignStatus.ASSIGNED && staffRole.getAssignedAt() == null) {
            staffRole.setAssignedAt(LocalDateTime.now());
        }
    }
}