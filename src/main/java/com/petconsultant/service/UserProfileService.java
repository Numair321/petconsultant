package com.petconsultant.service;

import com.petconsultant.request.ChangePasswordRequest;
import com.petconsultant.request.UserProfileRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.UserProfileResponse;

public interface UserProfileService {
    UserProfileResponse getProfile();
    ApiResponse updateProfile(UserProfileRequest request);
    ApiResponse uploadPhoto(String photoUrl);
    ApiResponse changePassword(ChangePasswordRequest request);
}
