package com.petconsultant.service;

import com.petconsultant.request.NotificationTokenRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.NotificationResponse;
import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getAllNotifications();
    ApiResponse markAsRead(Long id);
    ApiResponse updateFcmToken(NotificationTokenRequest request);
}
