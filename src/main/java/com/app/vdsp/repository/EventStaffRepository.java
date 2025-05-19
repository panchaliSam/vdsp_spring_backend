package com.app.vdsp.repository;

import com.app.vdsp.entity.EventStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventStaffRepository extends JpaRepository<EventStaff, Long> {
    List<EventStaff> findByStaffId(Long staffId);

    List<EventStaff> findByEventIdAndEventDateAndStaffIsNull(Long eventId, LocalDate eventDate);

    boolean existsByStaffIdAndEventDate(Long staffId, LocalDate eventDate);

    List<EventStaff> findByEventIdAndEventDate(Long eventId, LocalDate eventDate);
}