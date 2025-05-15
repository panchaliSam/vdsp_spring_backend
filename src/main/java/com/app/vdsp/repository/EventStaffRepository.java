package com.app.vdsp.repository;

import com.app.vdsp.entity.EventStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventStaffRepository extends JpaRepository<EventStaff, Long> {
    boolean existsByStaffIdAndEventDate(Long staffId, java.time.LocalDate eventDate);
}