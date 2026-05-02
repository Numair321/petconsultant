package com.petconsultant.service;

import com.petconsultant.constants.AppConstants;
import com.petconsultant.entity.NotificationEntity;
import com.petconsultant.entity.UserEntity;
import com.petconsultant.exception.ResourceNotFoundException;
import com.petconsultant.repository.NotificationRepository;
import com.petconsultant.repository.UserRepository;
import com.petconsultant.request.NotificationTokenRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.NotificationResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository         userRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                    UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository         = userRepository;
    }

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(email)) {
            return userRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("No user found in DB. Please register first."));
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));
    }

    @Override
    public List<NotificationResponse> getAllNotifications() {
        UserEntity user = getCurrentUser();
        List<NotificationEntity> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        List<NotificationResponse> result = new ArrayList<>();
        for (NotificationEntity n : notifications) {
            NotificationResponse res = new NotificationResponse();
            res.setId(n.getId());
            res.setTitle(n.getTitle());
            res.setMessage(n.getMessage());
            res.setIsRead(n.getIsRead());
            res.setCreatedAt(n.getCreatedAt());
            result.add(res);
        }
        return result;
    }

    @Override
    public ApiResponse markAsRead(Long id) {
        NotificationEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Notification marked as read");
        response.setData(null);
        return response;
    }

    @Override
    public ApiResponse updateFcmToken(NotificationTokenRequest request) {
        UserEntity user = getCurrentUser();
        user.setFcmToken(request.getFcmToken());
        userRepository.save(user);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("FCM token updated");
        response.setData(null);
        return response;
    }
}
