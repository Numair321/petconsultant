package com.petconsultant.service;

import com.petconsultant.request.LoginRequest;
import com.petconsultant.request.OtpVerifyRequest;
import com.petconsultant.request.RegisterRequest;
import com.petconsultant.request.ResetPasswordRequest;
import com.petconsultant.request.SendOtpRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.AuthResponse;

public interface AuthService {

    ApiResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    ApiResponse sendOtp(SendOtpRequest request);

    ApiResponse verifyOtp(OtpVerifyRequest request);

    ApiResponse resetPassword(ResetPasswordRequest request);

    ApiResponse logout(String token);
}
