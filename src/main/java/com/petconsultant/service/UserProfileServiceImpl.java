package com.petconsultant.service;

import com.petconsultant.constants.AppConstants;
import com.petconsultant.entity.UserEntity;
import com.petconsultant.exception.BadRequestException;
import com.petconsultant.exception.ResourceNotFoundException;
import com.petconsultant.exception.UnauthorizedException;
import com.petconsultant.repository.UserRepository;
import com.petconsultant.request.ChangePasswordRequest;
import com.petconsultant.request.UserProfileRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.UserProfileResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserProfileServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(email)) {
            return userRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("No user found in DB. Please register first."));
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));
    }

    @Override
    public UserProfileResponse getProfile() {
        UserEntity user = getCurrentUser();

        UserProfileResponse res = new UserProfileResponse();
        res.setId(user.getId());
        res.setFullName(user.getFullName());
        res.setEmail(user.getEmail());
        res.setPhone(user.getPhone());
        res.setRole(user.getRole() != null ? user.getRole().name() : null);
        res.setProfilePhoto(user.getProfilePhoto());
        return res;
    }

    @Override
    public ApiResponse updateProfile(UserProfileRequest request) {
        UserEntity user = getCurrentUser();
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        userRepository.save(user);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Profile updated successfully");
        response.setData(null);
        return response;
    }

    @Override
    public ApiResponse uploadPhoto(String photoUrl) {
        UserEntity user = getCurrentUser();
        user.setProfilePhoto(photoUrl);
        userRepository.save(user);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Photo updated successfully");
        response.setData(null);
        return response;
    }

    @Override
    public ApiResponse changePassword(ChangePasswordRequest request) {
        UserEntity user = getCurrentUser();
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new UnauthorizedException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Password changed successfully");
        response.setData(null);
        return response;
    }
}
