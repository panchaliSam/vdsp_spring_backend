package com.app.vdsp.controller;

import com.app.vdsp.entity.Album;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.service.AlbumService;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse<Album> create(@RequestBody Album album, @RequestHeader("Authorization") String authHeader) {
        return albumService.createAlbum(album, authHeader);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/{id}")
    public ApiResponse<Album> getById(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        return albumService.getAlbumById(id, authHeader);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping
    public ApiResponse<List<Album>> getAll(@RequestHeader("Authorization") String authHeader) {
        return albumService.getAllAlbums(authHeader);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @PutMapping("/{id}")
    public ApiResponse<Album> update(@PathVariable Long id, @RequestBody Album album, @RequestHeader("Authorization") String authHeader) {
        return albumService.updateAlbum(id, album, authHeader);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        return albumService.deleteAlbum(id, authHeader);
    }
}