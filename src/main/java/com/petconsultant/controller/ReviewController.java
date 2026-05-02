package com.petconsultant.controller;

import com.petconsultant.request.ReviewRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.ReviewResponse;
import com.petconsultant.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse> submitReview(@Valid @RequestBody ReviewRequest request) {
        ApiResponse response = reviewService.submitReview(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/vet/{vetId}")
    public ResponseEntity<ApiResponse> getVetReviews(@PathVariable Long vetId) {
        List<ReviewResponse> reviews = reviewService.getVetReviews(vetId);
        ApiResponse response = new ApiResponse("SUCCESS", "Reviews fetched", reviews);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
