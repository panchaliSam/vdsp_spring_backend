package com.app.vdsp.service;

import com.app.vdsp.dto.PresignedResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UploadService {
    List<PresignedResponseDto> generatePresignedUrls(List<String> filenames);
}
