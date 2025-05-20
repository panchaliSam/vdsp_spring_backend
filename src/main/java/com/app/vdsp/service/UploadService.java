package com.app.vdsp.service;

import com.app.vdsp.dto.PresignedResponseDto;
import com.app.vdsp.entity.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UploadService {
    ApiResponse<List<PresignedResponseDto>> generatePresignedUrls(List<String> filenames);
}
