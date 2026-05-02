package com.petconsultant.service;

import com.petconsultant.request.PaymentRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.PaymentResponse;
import java.util.List;

public interface PaymentService {
    ApiResponse initiatePayment(PaymentRequest request);
    ApiResponse verifyPayment(PaymentRequest request);
    List<PaymentResponse> getPaymentHistory();
}
