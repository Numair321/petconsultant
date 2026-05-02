package com.petconsultant.controller;

import com.petconsultant.request.PaymentRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.PaymentResponse;
import com.petconsultant.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<ApiResponse> initiatePayment(@Valid @RequestBody PaymentRequest request) {
        ApiResponse response = paymentService.initiatePayment(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse> verifyPayment(@Valid @RequestBody PaymentRequest request) {
        ApiResponse response = paymentService.verifyPayment(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse> getPaymentHistory() {
        List<PaymentResponse> payments = paymentService.getPaymentHistory();
        ApiResponse response = new ApiResponse("SUCCESS", "Payment history fetched", payments);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
