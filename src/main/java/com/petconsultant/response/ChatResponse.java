package com.petconsultant.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatResponse {

    private Long id;
    private Long userId;
    private Long vetId;
    private String vetName;
    private LocalDateTime createdAt;
}
