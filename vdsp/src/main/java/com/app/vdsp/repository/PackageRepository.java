package com.app.vdsp.repository;

import com.app.vdsp.entity.ReservationPackage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<ReservationPackage, Long> {
}
