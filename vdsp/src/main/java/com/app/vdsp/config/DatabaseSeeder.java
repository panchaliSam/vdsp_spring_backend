package com.app.vdsp.config;

import com.app.vdsp.entity.User;
import com.app.vdsp.repository.UserRepository;
import com.app.vdsp.type.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

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
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(adminUser);
        }
    }
}
