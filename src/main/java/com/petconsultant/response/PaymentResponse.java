package com.petconsultant.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {

    private Long id;
    private Long userId;
    private Long orderId;
    private Double amount;
    private String paymentMethod;
    private String transactionId;
    private String status;
    private LocalDateTime createdAt;
}
