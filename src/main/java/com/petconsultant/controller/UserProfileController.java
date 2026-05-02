package com.petconsultant.controller;

import com.petconsultant.request.ChangePasswordRequest;
import com.petconsultant.request.UserProfileRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.UserProfileResponse;
import com.petconsultant.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getProfile() {
        UserProfileResponse profile = userProfileService.getProfile();
        ApiResponse response = new ApiResponse("SUCCESS", "Profile fetched", profile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateProfile(@Valid @RequestBody UserProfileRequest request) {
        ApiResponse response = userProfileService.updateProfile(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/photo")
    public ResponseEntity<ApiResponse> uploadPhoto(@RequestParam String photoUrl) {
        ApiResponse response = userProfileService.uploadPhoto(photoUrl);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        ApiResponse response = userProfileService.changePassword(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
