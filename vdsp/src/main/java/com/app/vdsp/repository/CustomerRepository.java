package com.app.vdsp.repository;

import com.app.vdsp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findCustomerByEmailAndPassword(String email, String password);
}
