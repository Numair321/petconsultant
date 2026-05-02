package com.petconsultant.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewResponse {

    private Long id;
    private Long userId;
    private Long vetId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
