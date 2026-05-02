package com.petconsultant.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationResponse {

    private Long id;
    private String title;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
