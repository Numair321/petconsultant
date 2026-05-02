package com.petconsultant.controller;

import com.petconsultant.request.LoginRequest;
import com.petconsultant.request.OtpVerifyRequest;
import com.petconsultant.request.RegisterRequest;
import com.petconsultant.request.ResetPasswordRequest;
import com.petconsultant.request.SendOtpRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.AuthResponse;
import com.petconsultant.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /test/v1/api/auth/register
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        ApiResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // POST /test/v1/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        ApiResponse response = new ApiResponse();
        response.setStatus("SUCCESS");
        response.setMessage("Login successful");
        response.setData(authResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // POST /test/v1/api/auth/send-otp
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        ApiResponse response = authService.sendOtp(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // POST /test/v1/api/auth/verify-otp
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        ApiResponse response = authService.verifyOtp(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // PUT /test/v1/api/auth/reset-password
    @PutMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        ApiResponse response = authService.resetPassword(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // POST /test/v1/api/auth/logout
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        ApiResponse response = authService.logout(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
