package com.petconsultant.controller;

import com.petconsultant.request.NotificationTokenRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.NotificationResponse;
import com.petconsultant.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllNotifications() {
        List<NotificationResponse> list = notificationService.getAllNotifications();
        ApiResponse response = new ApiResponse("SUCCESS", "Notifications fetched", list);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/read/{id}")
    public ResponseEntity<ApiResponse> markAsRead(@PathVariable Long id) {
        ApiResponse response = notificationService.markAsRead(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/fcm-token")
    public ResponseEntity<ApiResponse> updateFcmToken(@Valid @RequestBody NotificationTokenRequest request) {
        ApiResponse response = notificationService.updateFcmToken(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
