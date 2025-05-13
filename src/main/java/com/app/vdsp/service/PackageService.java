package com.app.vdsp.service;

import com.app.vdsp.dto.PackageDto;

import java.util.List;
import java.util.Optional;

public interface PackageService {
    PackageDto createPackage(PackageDto packageDto, String authHeader);
    List<PackageDto> getAllPackages(String authHeader);
    Optional<PackageDto> getPackageById(Long id, String authHeader);
    void deletePackageById(Long id, String authHeader);
    void updatePackageById(PackageDto packages, Long id, String authHeader);
}
