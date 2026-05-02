package com.petconsultant.service;

import com.petconsultant.constants.AppConstants;
import com.petconsultant.entity.UserEntity;
import com.petconsultant.exception.BadRequestException;
import com.petconsultant.exception.ResourceNotFoundException;
import com.petconsultant.exception.UnauthorizedException;
import com.petconsultant.repository.UserRepository;
import com.petconsultant.request.LoginRequest;
import com.petconsultant.request.OtpVerifyRequest;
import com.petconsultant.request.RegisterRequest;
import com.petconsultant.request.ResetPasswordRequest;
import com.petconsultant.request.SendOtpRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.AuthResponse;
import com.petconsultant.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil         jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil         = jwtUtil;
    }

    // ========================= REGISTER =========================
    @Override
    public ApiResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(AppConstants.EMAIL_ALREADY_EXISTS);
        }

        UserEntity user = new UserEntity();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage(AppConstants.REGISTER_SUCCESS);
        response.setData(null);
        return response;
    }

    // ========================= LOGIN =========================
    @Override
    public AuthResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(AppConstants.INVALID_CREDENTIALS);
        }
        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new UnauthorizedException(AppConstants.ACCOUNT_INACTIVE);
        }

        String token = jwtUtil.generateToken(user.getEmail());

        AuthResponse response = new AuthResponse();
        response.setUserId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole().name());
        response.setProfilePhoto(user.getProfilePhoto());
        response.setToken(token);
        return response;
    }

    // ========================= SEND OTP =========================
    @Override
    public ApiResponse sendOtp(SendOtpRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));

        String otp = generateOtp();

        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(AppConstants.OTP_EXPIRY_MINUTES));
        userRepository.save(user);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage(AppConstants.OTP_SENT);
        response.setData("OTP: " + otp + " (valid for 5 minutes)");
        return response;
    }

    // ========================= VERIFY OTP =========================
    @Override
    public ApiResponse verifyOtp(OtpVerifyRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));

        if (user.getOtp() == null
                || !user.getOtp().equals(request.getOtp())
                || LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            throw new BadRequestException(AppConstants.INVALID_OTP);
        }

        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage(AppConstants.OTP_VERIFIED);
        response.setData(null);
        return response;
    }

    // ========================= RESET PASSWORD =========================
    @Override
    public ApiResponse resetPassword(ResetPasswordRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));

        if (user.getOtp() == null
                || !user.getOtp().equals(request.getOtp())
                || LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            throw new BadRequestException(AppConstants.INVALID_OTP);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage(AppConstants.PASSWORD_RESET);
        response.setData(null);
        return response;
    }

    // ========================= LOGOUT =========================
    @Override
    public ApiResponse logout(String token) {
        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage(AppConstants.LOGOUT_SUCCESS);
        response.setData(null);
        return response;
    }

    // ========================= HELPER =========================
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
