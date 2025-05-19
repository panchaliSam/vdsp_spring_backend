package com.app.vdsp.service.Impl;

import com.app.vdsp.entity.Album;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.AlbumRepository;
import com.app.vdsp.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final AuthorizationHelper authorizationHelper;

    @Override
    public ApiResponse<Album> createAlbum(Album album, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        return new ApiResponse<>(true, "Album created", albumRepository.save(album));
    }

    @Override
    public ApiResponse<Album> getAlbumById(Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found"));
        return new ApiResponse<>(true, "Album fetched", album);
    }

    @Override
    public ApiResponse<List<Album>> getAllAlbums(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        return new ApiResponse<>(true, "All albums", albumRepository.findAll());
    }

    @Override
    public ApiResponse<Album> updateAlbum(Long id, Album updatedAlbum, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found"));

        album.setName(updatedAlbum.getName());
        album.setCoverPhoto(updatedAlbum.getCoverPhoto());

        return new ApiResponse<>(true, "Album updated", albumRepository.save(album));
    }

    @Override
    public ApiResponse<String> deleteAlbum(Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        albumRepository.deleteById(id);
        return new ApiResponse<>(true, "Album deleted", null);
    }
}