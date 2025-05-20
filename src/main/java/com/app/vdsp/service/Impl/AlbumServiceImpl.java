package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.AlbumDto;
import com.app.vdsp.dto.AlbumSummaryDto;
import com.app.vdsp.dto.ImageDto;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final AuthorizationHelper authorizationHelper;

    @Override
    public ApiResponse<AlbumDto> createAlbum(AlbumDto dto, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        if (dto.getCoverPhoto() == null || dto.getCoverPhoto().isBlank()) {
            dto.setCoverPhoto("https://placehold.co/600x400");
        }

        Album saved = albumRepository.save(AlbumDto.toEntity(dto));
        return new ApiResponse<>(true, "Album created", AlbumDto.fromEntity(saved));
    }

    @Override
    public ApiResponse<AlbumDto> getAlbumById(Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found"));
        return new ApiResponse<>(true, "Album fetched", AlbumDto.fromEntity(album));
    }

    @Override
    public ApiResponse<List<AlbumSummaryDto>> getAllAlbums(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        List<AlbumSummaryDto> result = albumRepository.findAll().stream()
                .map(AlbumSummaryDto::fromEntity)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "All albums", result);
    }


    @Override
    public ApiResponse<AlbumDto> updateAlbum(Long id, AlbumDto dto, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found"));

        if (dto.getCoverPhoto() == null || dto.getCoverPhoto().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cover photo is required");
        }

        if (dto.getName() != null && !dto.getName().isBlank()) {
            album.setName(dto.getName());
        }

        album.setCoverPhoto(dto.getCoverPhoto());

        Album updated = albumRepository.save(album);
        return new ApiResponse<>(true, "Album updated", AlbumDto.fromEntity(updated));
    }

    @Override
    public ApiResponse<String> deleteAlbum(Long id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        albumRepository.deleteById(id);
        return new ApiResponse<>(true, "Album deleted", null);
    }
}