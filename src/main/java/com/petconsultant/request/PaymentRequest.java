package com.petconsultant.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Amount is required")
    private Double amount;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    private String transactionId;
}
