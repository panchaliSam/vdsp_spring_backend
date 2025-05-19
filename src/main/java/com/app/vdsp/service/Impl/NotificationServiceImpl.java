package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.NotificationDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.NotificationRepository;
import com.app.vdsp.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final AuthorizationHelper authorizationHelper;

    @Override
    public ApiResponse<List<NotificationDto>> getMyNotifications(String authHeader) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);
        Long userId = authorizationHelper.extractUserId(authHeader);

        List<NotificationDto> notifications = notificationRepository.findByUserId(userId)
                .stream()
                .map(NotificationDto::fromEntity)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Notifications fetched successfully", notifications);
    }
}