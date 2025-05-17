package com.app.vdsp.controller;

import com.app.vdsp.dto.PackageDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.service.PackageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private static final Logger log = LoggerFactory.getLogger(PackageController.class);
    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PackageDto>> create(@RequestBody PackageDto packageDto,
                                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Received request to create a package");
        return ResponseEntity.ok(packageService.createPackage(packageDto, authHeader));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PackageDto>>> getAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Received request to get all packages");
        return ResponseEntity.ok(packageService.getAllPackages(authHeader));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PackageDto>> getById(@PathVariable Long id,
                                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Received request to get package by id: {}", id);
        return ResponseEntity.ok(packageService.getPackageById(id, authHeader));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id,
                                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Received request to delete package with id: {}", id);
        return ResponseEntity.ok(packageService.deletePackageById(id, authHeader));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> patchUpdate(@RequestBody PackageDto packageDto,
                                                           @PathVariable Long id,
                                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Received request to patch update package with id: {}", id);
        return ResponseEntity.ok(packageService.updatePackageById(packageDto, id, authHeader));
    }
}