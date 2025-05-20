package com.app.vdsp.controller;

import com.app.vdsp.dto.AlbumDto;
import com.app.vdsp.dto.AlbumSummaryDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @PostMapping
    public ApiResponse<AlbumDto> create(
            @RequestBody AlbumDto dto,
            @RequestHeader("Authorization") String authHeader) {
        return albumService.createAlbum(dto, authHeader);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/{id}")
    public ApiResponse<AlbumDto> getById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        return albumService.getAlbumById(id, authHeader);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping
    public ApiResponse<List<AlbumSummaryDto>> getAll(
            @RequestHeader("Authorization") String authHeader) {
        return albumService.getAllAlbums(authHeader);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<AlbumDto>> patchAlbum(
            @PathVariable Long id,
            @RequestBody AlbumDto dto,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(albumService.updateAlbum(id, dto, authHeader));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        return albumService.deleteAlbum(id, authHeader);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/event/{eventId}")
    public ApiResponse<AlbumDto> getByEventId(
            @PathVariable Long eventId,
            @RequestHeader("Authorization") String authHeader) {
        return albumService.getAlbumByEventId(eventId, authHeader);
    }
}