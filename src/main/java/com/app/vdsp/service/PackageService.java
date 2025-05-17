package com.app.vdsp.service;

import com.app.vdsp.dto.PackageDto;
import com.app.vdsp.entity.ApiResponse;

import java.util.List;

public interface PackageService {
    ApiResponse<PackageDto> createPackage(PackageDto packageDto, String authHeader);
    ApiResponse<List<PackageDto>> getAllPackages(String authHeader);
    ApiResponse<PackageDto> getPackageById(Long id, String authHeader);
    ApiResponse<String> deletePackageById(Long id, String authHeader);
    ApiResponse<String> updatePackageById(PackageDto packages, Long id, String authHeader);
}