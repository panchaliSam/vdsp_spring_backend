package com.app.vdsp.config;

import com.app.vdsp.entity.ReservationPackage;
import com.app.vdsp.entity.User;
import com.app.vdsp.repository.PackageRepository;
import com.app.vdsp.repository.UserRepository;
import com.app.vdsp.type.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@example.com")) {
            String hashedPassword = passwordEncoder.encode("admin123");
            User adminUser = User.builder()
                    .firstName("Admin")
                    .lastName("User")
                    .email("admin@example.com")
                    .password(hashedPassword)
                    .phoneNumber("1234567890")
                    .role(RoleType.ROLE_ADMIN)
                    .build();
            userRepository.save(adminUser);
        }

        if (!packageRepository.existsById(1L)) {
            ReservationPackage samplePackage = ReservationPackage.builder()
                    .name("Standard Package")
                    .description("A standard package with basic features.")
                    .price(new BigDecimal("199.99"))
                    .build();
            packageRepository.save(samplePackage);
        }
    }
}
