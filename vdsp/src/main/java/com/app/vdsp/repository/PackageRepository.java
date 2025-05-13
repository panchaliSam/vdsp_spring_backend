package com.app.vdsp.repository;

import com.app.vdsp.entity.ReservationPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackageRepository extends JpaRepository<ReservationPackage, Long> {
    Optional<ReservationPackage> findByName(String name);
}
