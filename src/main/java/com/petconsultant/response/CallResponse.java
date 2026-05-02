package com.petconsultant.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CallResponse {

    private Long id;
    private Long userId;
    private Long vetId;
    private String callType;
    private String token;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
}
