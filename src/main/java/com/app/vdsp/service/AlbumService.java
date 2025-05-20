package com.app.vdsp.service;

import com.app.vdsp.dto.AlbumDto;
import com.app.vdsp.dto.AlbumSummaryDto;
import com.app.vdsp.entity.ApiResponse;

import java.util.List;

public interface AlbumService {
    ApiResponse<AlbumDto> createAlbum(AlbumDto dto, String authHeader);
    ApiResponse<AlbumDto> getAlbumById(Long id, String authHeader);
    ApiResponse<List<AlbumSummaryDto>> getAllAlbums(String authHeader);
    ApiResponse<AlbumDto> updateAlbum(Long id, AlbumDto dto, String authHeader);
    ApiResponse<String> deleteAlbum(Long id, String authHeader);
}