package com.app.vdsp.controller;

import com.app.vdsp.dto.PackageDto;
import com.app.vdsp.service.PackageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/packages")
public class PackageController {

    private static final Logger log = LoggerFactory.getLogger(PackageController.class);

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping("/create")
    public ResponseEntity<PackageDto> create(@RequestBody PackageDto packageDto,
                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Received request to create a package: {}", packageDto);

        try {
            PackageDto createdPackage = packageService.createPackage(packageDto, authHeader);
            return ResponseEntity.ok(createdPackage);
        } catch (Exception e) {
            log.error("Error occurred while creating package: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}
