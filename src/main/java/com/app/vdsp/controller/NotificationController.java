package com.app.vdsp.controller;

import com.app.vdsp.dto.NotificationDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getMyNotifications(
            @RequestHeader("Authorization") String authHeader) {
        ApiResponse<List<NotificationDto>> response = notificationService.getMyNotifications(authHeader);
        return ResponseEntity.ok(response);
    }
}