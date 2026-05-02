package com.petconsultant.response;

import lombok.Data;

@Data
public class UserProfileResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String profilePhoto;
}
