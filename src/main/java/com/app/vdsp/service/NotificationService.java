package com.app.vdsp.service;

import com.app.vdsp.dto.NotificationDto;
import com.app.vdsp.entity.ApiResponse;

import java.util.List;

public interface NotificationService {
    ApiResponse<List<NotificationDto>> getMyNotifications(String authHeader);
}