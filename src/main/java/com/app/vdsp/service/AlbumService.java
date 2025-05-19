package com.app.vdsp.service;

import com.app.vdsp.entity.Album;
import com.app.vdsp.entity.ApiResponse;

import java.util.List;

public interface AlbumService {
    ApiResponse<Album> createAlbum(Album album, String authHeader);
    ApiResponse<Album> getAlbumById(Long id, String authHeader);
    ApiResponse<List<Album>> getAllAlbums(String authHeader);
    ApiResponse<Album> updateAlbum(Long id, Album album, String authHeader);
    ApiResponse<String> deleteAlbum(Long id, String authHeader);
}