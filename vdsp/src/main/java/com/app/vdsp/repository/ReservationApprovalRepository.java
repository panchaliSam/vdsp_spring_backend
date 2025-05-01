package com.app.vdsp.repository;

import com.app.vdsp.entity.ReservationApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationApprovalRepository extends JpaRepository<ReservationApproval, Long> {
    @Query("SELECT ra FROM ReservationApproval ra WHERE ra.user.id = :userId")
    List<ReservationApproval> findByUserId(@Param("userId") Long userId);
}
