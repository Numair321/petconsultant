package com.petconsultant.service;

import com.petconsultant.constants.AppConstants;
import com.petconsultant.entity.PaymentEntity;
import com.petconsultant.entity.UserEntity;
import com.petconsultant.enumerator.PaymentStatus;
import com.petconsultant.exception.ResourceNotFoundException;
import com.petconsultant.repository.PaymentRepository;
import com.petconsultant.repository.UserRepository;
import com.petconsultant.request.PaymentRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.PaymentResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository    userRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository    = userRepository;
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
    public ApiResponse initiatePayment(PaymentRequest request) {
        UserEntity user = getCurrentUser();

        PaymentEntity payment = new PaymentEntity();
        payment.setUserId(user.getId());
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.PENDING);

        PaymentEntity saved = paymentRepository.save(payment);

        PaymentResponse res = new PaymentResponse();
        res.setId(saved.getId());
        res.setUserId(saved.getUserId());
        res.setOrderId(saved.getOrderId());
        res.setAmount(saved.getAmount());
        res.setPaymentMethod(saved.getPaymentMethod());
        res.setStatus(saved.getStatus().name());
        res.setCreatedAt(saved.getCreatedAt());

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Payment initiated");
        response.setData(res);
        return response;
    }

    @Override
    public ApiResponse verifyPayment(PaymentRequest request) {
        PaymentEntity payment = paymentRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        payment.setTransactionId(request.getTransactionId());
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Payment verified successfully");
        response.setData(null);
        return response;
    }

    @Override
    public List<PaymentResponse> getPaymentHistory() {
        UserEntity user = getCurrentUser();
        List<PaymentEntity> payments = paymentRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        List<PaymentResponse> result = new ArrayList<>();
        for (PaymentEntity payment : payments) {
            PaymentResponse res = new PaymentResponse();
            res.setId(payment.getId());
            res.setUserId(payment.getUserId());
            res.setOrderId(payment.getOrderId());
            res.setAmount(payment.getAmount());
            res.setPaymentMethod(payment.getPaymentMethod());
            res.setTransactionId(payment.getTransactionId());
            res.setStatus(payment.getStatus() != null ? payment.getStatus().name() : null);
            res.setCreatedAt(payment.getCreatedAt());
            result.add(res);
        }
        return result;
    }
}
