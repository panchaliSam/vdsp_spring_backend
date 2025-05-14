package com.app.vdsp.repository;

import com.app.vdsp.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByReservation_UserId(Long userId);
    List<Event> findByEventDate(java.time.LocalDate eventDate);
}