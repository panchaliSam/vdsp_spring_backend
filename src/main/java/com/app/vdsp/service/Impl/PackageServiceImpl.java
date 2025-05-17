package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.PackageDto;
import com.app.vdsp.entity.ApiResponse;
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
    public ApiResponse<PackageDto> createPackage(PackageDto packageDto, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        log.info("Creating package: {}", packageDto);

        ReservationPackage reservationPackage = new ReservationPackage();
        reservationPackage.setName(packageDto.getName());
        reservationPackage.setDescription(packageDto.getDescription());
        reservationPackage.setPrice(packageDto.getPrice());

        packageRepository.save(reservationPackage);
        return new ApiResponse<>(true, "Package created successfully", packageDto);
    }

    @Override
    public ApiResponse<List<PackageDto>> getAllPackages(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        log.info("Fetching all packages");

        List<PackageDto> packages = packageRepository.findAll().stream()
                .map(PackageDto::fromEntity)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Fetched all packages", packages);
    }

    @Override
    public ApiResponse<PackageDto> getPackageById(Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        log.info("Fetching package by id: {}", id);

        Optional<ReservationPackage> pkg = packageRepository.findById(id);
        return pkg.map(reservationPackage -> new ApiResponse<>(true, "Package found", PackageDto.fromEntity(reservationPackage)))
                .orElseGet(() -> new ApiResponse<>(false, "Package not found", null));
    }

    @Override
    public ApiResponse<String> deletePackageById(Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        log.info("Deleting package with id: {}", id);

        if (packageRepository.existsById(id)) {
            packageRepository.deleteById(id);
            return new ApiResponse<>(true, "Package deleted successfully", null);
        } else {
            return new ApiResponse<>(false, "Package not found", null);
        }
    }

    @Override
    public ApiResponse<String> updatePackageById(PackageDto packageDto, Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        log.info("Updating package with id: {}", id);

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
        return new ApiResponse<>(true, "Package updated successfully", null);
    }
}