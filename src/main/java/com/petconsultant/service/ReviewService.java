package com.petconsultant.service;

import com.petconsultant.request.ReviewRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.ReviewResponse;
import java.util.List;

public interface ReviewService {
    ApiResponse submitReview(ReviewRequest request);
    List<ReviewResponse> getVetReviews(Long vetId);
}
