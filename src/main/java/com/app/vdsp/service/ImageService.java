package com.app.vdsp.service;

import com.app.vdsp.entity.Image;
import com.app.vdsp.entity.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface ImageService {
    ApiResponse<Image> createImage(Image image, String authHeader);
    ApiResponse<Image> getImageById(UUID id, String authHeader);
    ApiResponse<List<Image>> getImagesByAlbum(Long albumId, String authHeader);
    ApiResponse<Image> updateImage(UUID id, Image image, String authHeader);
    ApiResponse<String> deleteImage(UUID id, String authHeader);
}