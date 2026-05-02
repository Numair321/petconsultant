package com.petconsultant.service;

import com.petconsultant.constants.AppConstants;
import com.petconsultant.entity.CallEntity;
import com.petconsultant.entity.UserEntity;
import com.petconsultant.enumerator.CallType;
import com.petconsultant.exception.ResourceNotFoundException;
import com.petconsultant.repository.CallRepository;
import com.petconsultant.repository.UserRepository;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.CallResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CallServiceImpl implements CallService {

    private final CallRepository callRepository;
    private final UserRepository userRepository;

    public CallServiceImpl(CallRepository callRepository, UserRepository userRepository) {
        this.callRepository = callRepository;
        this.userRepository = userRepository;
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
    public ApiResponse initiateCall(Long vetId, CallType callType) {
        UserEntity user = getCurrentUser();

        CallEntity call = new CallEntity();
        call.setUserId(user.getId());
        call.setVetId(vetId);
        call.setCallType(callType);
        call.setToken(UUID.randomUUID().toString());
        call.setStatus("INITIATED");
        call.setStartTime(LocalDateTime.now());

        CallEntity saved = callRepository.save(call);

        CallResponse res = new CallResponse();
        res.setId(saved.getId());
        res.setUserId(saved.getUserId());
        res.setVetId(saved.getVetId());
        res.setCallType(saved.getCallType().name());
        res.setToken(saved.getToken());
        res.setStatus(saved.getStatus());
        res.setStartTime(saved.getStartTime());
        res.setCreatedAt(saved.getCreatedAt());

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Call initiated");
        response.setData(res);
        return response;
    }

    @Override
    public ApiResponse endCall(Long callId) {
        CallEntity call = callRepository.findById(callId)
                .orElseThrow(() -> new ResourceNotFoundException("Call not found"));
        call.setStatus("ENDED");
        call.setEndTime(LocalDateTime.now());
        callRepository.save(call);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Call ended");
        response.setData(null);
        return response;
    }

    @Override
    public List<CallResponse> getCallHistory() {
        UserEntity user = getCurrentUser();
        List<CallEntity> calls = callRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        List<CallResponse> result = new ArrayList<>();
        for (CallEntity call : calls) {
            CallResponse res = new CallResponse();
            res.setId(call.getId());
            res.setUserId(call.getUserId());
            res.setVetId(call.getVetId());
            res.setCallType(call.getCallType() != null ? call.getCallType().name() : null);
            res.setToken(call.getToken());
            res.setStatus(call.getStatus());
            res.setStartTime(call.getStartTime());
            res.setEndTime(call.getEndTime());
            res.setCreatedAt(call.getCreatedAt());
            result.add(res);
        }
        return result;
    }
}
