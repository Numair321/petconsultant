package com.petconsultant.response;

import lombok.Data;

@Data
public class ApiResponse {

    private String status;
    private String message;
    private Object data;

    public ApiResponse() {}

    public ApiResponse(String status, String message, Object data) {
        this.status  = status;
        this.message = message;
        this.data    = data;
    }
}
