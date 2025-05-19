package com.app.vdsp.service;

import com.app.vdsp.dto.ImageDto;
import com.app.vdsp.entity.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface ImageService {
    ApiResponse<ImageDto> createImage(ImageDto dto, String authHeader);
    ApiResponse<ImageDto> getImageById(UUID id, String authHeader);
    ApiResponse<List<ImageDto>> getImagesByAlbum(Long albumId, String authHeader);
    ApiResponse<ImageDto> updateImage(UUID id, ImageDto dto, String authHeader);
    ApiResponse<String> deleteImage(UUID id, String authHeader);
}