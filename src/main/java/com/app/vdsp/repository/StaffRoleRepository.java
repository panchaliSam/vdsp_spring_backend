package com.app.vdsp.repository;

import com.app.vdsp.entity.StaffRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StaffRoleRepository extends JpaRepository<StaffRole, Long> {
    @Query("SELECT sr FROM StaffRole sr WHERE sr.staff.id = :staffId")
    List<StaffRole> findByStaffId(@Param("staffId") Long staffId);
}
