package com.app.vdsp.repository;

import com.app.vdsp.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
}