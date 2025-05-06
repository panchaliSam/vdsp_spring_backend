package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.PackageDto;
import com.app.vdsp.entity.ReservationPackage;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.PackageRepository;
import com.app.vdsp.service.PackageService;
import com.app.vdsp.utils.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PackageServiceImpl implements PackageService {

    private static final Logger log = LoggerFactory.getLogger(PackageServiceImpl.class);

    private final PackageRepository packageRepository;

    public PackageServiceImpl(PackageRepository packageRepository, JWTService jwtService) {
        this.packageRepository = packageRepository;
    }

    @Override
    public PackageDto createPackage(PackageDto packageDto, String authHeader) {
        log.info("Creating package {}", packageDto);

        try {
            AuthorizationHelper.ensureAuthorizationHeader(authHeader);

            ReservationPackage reservationPackage = new ReservationPackage();
            reservationPackage.setName(packageDto.getName());
            reservationPackage.setDescription(packageDto.getDescription());
            reservationPackage.setPrice(packageDto.getPrice());

            packageRepository.save(reservationPackage);

            log.info("Package created successfully");
            return packageDto;
        } catch (ResponseStatusException e) {
            log.error("Business error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred while creating package", e);
        }
    }


    @Override
    public List<PackageDto> getAllPackages() {
        return List.of();
    }

    @Override
    public Optional<PackageDto> getPackageById(Long id) {
        return Optional.empty();
    }

    @Override
    public void deletePackageById(Long id) {

    }

    @Override
    public void updatePackageById(PackageDto packages, Long id) {

    }
}
