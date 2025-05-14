package com.app.vdsp.entity;

import com.app.vdsp.type.StaffAssignStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(StaffRoleListener.class)
@Table(name = "staff_roles", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"staff_id", "role_id"}, name = "unique_staff_role")
})
public class StaffRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false, referencedColumnName = "id")
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "assign_status", nullable = false, length = 20)
    private StaffAssignStatus assignStatus;

    @Column(name = "assigned_at", updatable = false)
    private LocalDateTime assignedAt;
}