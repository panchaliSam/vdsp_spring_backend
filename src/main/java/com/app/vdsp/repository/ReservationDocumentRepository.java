package com.app.vdsp.repository;

import com.app.vdsp.entity.ReservationDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationDocumentRepository extends JpaRepository<ReservationDocument, Long> {
    Optional<ReservationDocument> findByReservationId(Long reservationId);
}