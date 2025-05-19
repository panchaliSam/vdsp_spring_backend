package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.ImageDto;
import com.app.vdsp.entity.Album;
import com.app.vdsp.entity.Image;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.AlbumRepository;
import com.app.vdsp.repository.ImageRepository;
import com.app.vdsp.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final AlbumRepository albumRepository;
    private final AuthorizationHelper authorizationHelper;

    @Override
    public ApiResponse<ImageDto> createImage(ImageDto dto, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        Album album = albumRepository.findById(dto.getAlbumId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found"));
        Image saved = imageRepository.save(ImageDto.toEntity(dto, album));
        return new ApiResponse<>(true, "Image created", ImageDto.fromEntity(saved));
    }

    @Override
    public ApiResponse<ImageDto> getImageById(UUID id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
        return new ApiResponse<>(true, "Image fetched", ImageDto.fromEntity(image));
    }

    @Override
    public ApiResponse<List<ImageDto>> getImagesByAlbum(Long albumId, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found"));
        List<ImageDto> result = album.getImages().stream()
                .map(ImageDto::fromEntity)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "Images for album", result);
    }

    @Override
    public ApiResponse<ImageDto> updateImage(UUID id, ImageDto dto, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
        image.setPath(dto.getPath());
        return new ApiResponse<>(true, "Image updated", ImageDto.fromEntity(imageRepository.save(image)));
    }

    @Override
    public ApiResponse<String> deleteImage(UUID id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        imageRepository.deleteById(id);
        return new ApiResponse<>(true, "Image deleted", null);
    }
}