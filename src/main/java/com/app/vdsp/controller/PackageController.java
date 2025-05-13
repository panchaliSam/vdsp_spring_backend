package com.app.vdsp.controller;

import com.app.vdsp.dto.PackageDto;
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
    public ResponseEntity<PackageDto> create(@RequestBody PackageDto packageDto,
                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Received request to create a package");
        PackageDto createdPackage = packageService.createPackage(packageDto, authHeader);
        return ResponseEntity.ok(createdPackage);
    }

    @GetMapping
    public ResponseEntity<List<PackageDto>> getAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Received request to get all packages");
        List<PackageDto> packages = packageService.getAllPackages(authHeader);
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageDto> getById(@PathVariable Long id,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Received request to get package by id: {}", id);
        return packageService.getPackageById(id, authHeader)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Received request to delete package with id: {}", id);
        packageService.deletePackageById(id, authHeader);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchUpdate(@RequestBody PackageDto packageDto,
                                            @PathVariable Long id,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Received request to patch update package with id: {}", id);
        packageService.updatePackageById(packageDto, id, authHeader);
        return ResponseEntity.ok().build();
    }

}
