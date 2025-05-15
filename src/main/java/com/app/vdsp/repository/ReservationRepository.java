package com.app.vdsp.repository;

import com.app.vdsp.entity.Reservation;
import com.app.vdsp.type.SessionType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByEventDate(LocalDate eventDate);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Reservation r WHERE r.eventDate = :eventDate AND r.sessionType IN :sessionTypes")
    List<Reservation> findConflictingReservationsWithLock(
            @Param("eventDate") LocalDate eventDate,
            @Param("sessionTypes") List<SessionType> sessionTypes);
}
