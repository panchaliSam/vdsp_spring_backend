package com.app.vdsp.service.Impl;

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

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final AlbumRepository albumRepository;
    private final AuthorizationHelper authorizationHelper;

    @Override
    public ApiResponse<Image> createImage(Image image, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        if (image.getAlbum() != null && image.getAlbum().getId() != null) {
            Album album = albumRepository.findById(image.getAlbum().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found"));
            image.setAlbum(album);
        }
        return new ApiResponse<>(true, "Image created", imageRepository.save(image));
    }

    @Override
    public ApiResponse<Image> getImageById(UUID id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
        return new ApiResponse<>(true, "Image fetched", image);
    }

    @Override
    public ApiResponse<List<Image>> getImagesByAlbum(Long albumId, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found"));
        return new ApiResponse<>(true, "Images for album", album.getImages());
    }

    @Override
    public ApiResponse<Image> updateImage(UUID id, Image updatedImage, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
        image.setPath(updatedImage.getPath());
        return new ApiResponse<>(true, "Image updated", imageRepository.save(image));
    }

    @Override
    public ApiResponse<String> deleteImage(UUID id, String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        imageRepository.deleteById(id);
        return new ApiResponse<>(true, "Image deleted", null);
    }
}