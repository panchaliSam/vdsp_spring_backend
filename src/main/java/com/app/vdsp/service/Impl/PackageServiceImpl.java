package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.PackageDto;
import com.app.vdsp.entity.ReservationPackage;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.PackageRepository;
import com.app.vdsp.service.PackageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PackageServiceImpl implements PackageService {

    private static final Logger log = LoggerFactory.getLogger(PackageServiceImpl.class);
    private final PackageRepository packageRepository;

    public PackageServiceImpl(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public PackageDto createPackage(PackageDto packageDto, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        log.info("Creating package: {}", packageDto);

        ReservationPackage reservationPackage = new ReservationPackage();
        reservationPackage.setName(packageDto.getName());
        reservationPackage.setDescription(packageDto.getDescription());
        reservationPackage.setPrice(packageDto.getPrice());

        packageRepository.save(reservationPackage);
        return packageDto;
    }

    @Override
    public List<PackageDto> getAllPackages(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        log.info("Fetching all packages");

        return packageRepository.findAll().stream()
                .map(PackageDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PackageDto> getPackageById(Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        log.info("Fetching package by id: {}", id);

        return packageRepository.findById(id).map(PackageDto::fromEntity);
    }

    @Override
    public void deletePackageById(Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        log.info("Deleting package with id: {}", id);

        if (packageRepository.existsById(id)) {
            packageRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found");
        }
    }

    @Override
    public void updatePackageById(PackageDto packageDto, Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        log.info("Patching package with id: {}", id);

        ReservationPackage reservationPackage = packageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found"));

        if (packageDto.getName() != null) {
            reservationPackage.setName(packageDto.getName());
        }
        if (packageDto.getDescription() != null) {
            reservationPackage.setDescription(packageDto.getDescription());
        }
        if (packageDto.getPrice() != null) {
            reservationPackage.setPrice(packageDto.getPrice());
        }

        packageRepository.save(reservationPackage);
    }

}
