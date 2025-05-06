package com.app.vdsp.service;

import com.app.vdsp.dto.PackageDto;

import java.util.List;
import java.util.Optional;

public interface PackageService {
    PackageDto createPackage(PackageDto packageDto, String authHeader);
    List<PackageDto> getAllPackages();
    Optional<PackageDto> getPackageById(Long id);
    void deletePackageById(Long id);
    void updatePackageById(PackageDto packages, Long id);
}
