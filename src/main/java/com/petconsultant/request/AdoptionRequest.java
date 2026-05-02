package com.petconsultant.request;

import lombok.Data;

@Data
public class AdoptionRequest {
    private Long petId;
    private String message;
}
