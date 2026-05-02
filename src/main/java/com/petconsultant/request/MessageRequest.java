package com.petconsultant.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequest {

    @NotNull(message = "Chat ID is required")
    private Long chatId;

    @NotBlank(message = "Message is required")
    private String message;
}
