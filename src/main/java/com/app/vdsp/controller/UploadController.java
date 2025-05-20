package com.app.vdsp.controller;


import com.app.vdsp.dto.PresignedResponseDto;
import com.app.vdsp.service.UploadService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/batch")
    public List<PresignedResponseDto> createPresigned(@RequestBody List<String> filenames) {
        return uploadService.generatePresignedUrls(filenames);
    }
}
