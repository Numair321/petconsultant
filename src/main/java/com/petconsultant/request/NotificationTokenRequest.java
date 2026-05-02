package com.petconsultant.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NotificationTokenRequest {

    @NotBlank(message = "FCM token is required")
    private String fcmToken;
}
