package com.app.vdsp.repository;

import com.app.vdsp.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByEventDate(LocalDate eventDate);
}
