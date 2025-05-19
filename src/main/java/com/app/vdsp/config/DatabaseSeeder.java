package com.app.vdsp.config;

import com.app.vdsp.entity.*;
import com.app.vdsp.repository.*;
import com.app.vdsp.type.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Admin user seed
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

        // Reservation package seed
        if (!packageRepository.existsById(1L)) {
            ReservationPackage samplePackage = ReservationPackage.builder()
                    .name("Standard Package")
                    .description("A standard package with basic features.")
                    .price(new BigDecimal("199.99"))
                    .build();
            packageRepository.save(samplePackage);
        }

        // Album & Images seed
        if (albumRepository.count() == 0) {
            Album album = Album.builder()
                    .name("Nature Photography")
                    .coverPhoto("nature-cover.jpg")
                    .build();

            // Save album first to get ID
            album = albumRepository.save(album);

            Image img1 = Image.builder()
                    .path("nature1.jpg")
                    .album(album)
                    .build();

            Image img2 = Image.builder()
                    .path("nature2.jpg")
                    .album(album)
                    .build();

            Image img3 = Image.builder()
                    .path("nature3.jpg")
                    .album(album)
                    .build();

            imageRepository.saveAll(List.of(img1, img2, img3));
        }
    }
}