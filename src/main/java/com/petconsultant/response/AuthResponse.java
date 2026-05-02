package com.petconsultant.response;

import lombok.Data;

@Data
public class AuthResponse {

    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String profilePhoto;
    private String token;
}
