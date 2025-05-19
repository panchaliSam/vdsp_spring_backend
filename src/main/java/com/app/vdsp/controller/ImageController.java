package com.app.vdsp.controller;

import com.app.vdsp.dto.ImageDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @PostMapping
    public ApiResponse<ImageDto> create(@RequestBody ImageDto dto, @RequestHeader("Authorization") String authHeader) {
        return imageService.createImage(dto, authHeader);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/{id}")
    public ApiResponse<ImageDto> getById(@PathVariable UUID id, @RequestHeader("Authorization") String authHeader) {
        return imageService.getImageById(id, authHeader);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/album/{albumId}")
    public ApiResponse<List<ImageDto>> getByAlbum(@PathVariable Long albumId, @RequestHeader("Authorization") String authHeader) {
        return imageService.getImagesByAlbum(albumId, authHeader);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @PutMapping("/{id}")
    public ApiResponse<ImageDto> update(@PathVariable UUID id, @RequestBody ImageDto dto, @RequestHeader("Authorization") String authHeader) {
        return imageService.updateImage(id, dto, authHeader);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable UUID id, @RequestHeader("Authorization") String authHeader) {
        return imageService.deleteImage(id, authHeader);
    }
}