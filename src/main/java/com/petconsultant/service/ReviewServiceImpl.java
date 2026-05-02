package com.petconsultant.service;

import com.petconsultant.constants.AppConstants;
import com.petconsultant.entity.ReviewEntity;
import com.petconsultant.entity.UserEntity;
import com.petconsultant.exception.ResourceNotFoundException;
import com.petconsultant.repository.ReviewRepository;
import com.petconsultant.repository.UserRepository;
import com.petconsultant.request.ReviewRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.ReviewResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository   userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository   = userRepository;
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
    public ApiResponse submitReview(ReviewRequest request) {
        UserEntity user = getCurrentUser();

        ReviewEntity review = new ReviewEntity();
        review.setUserId(user.getId());
        review.setVetId(request.getVetId());
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        reviewRepository.save(review);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Review submitted successfully");
        response.setData(null);
        return response;
    }

    @Override
    public List<ReviewResponse> getVetReviews(Long vetId) {
        List<ReviewEntity> reviews = reviewRepository.findByVetId(vetId);
        List<ReviewResponse> result = new ArrayList<>();
        for (ReviewEntity review : reviews) {
            ReviewResponse res = new ReviewResponse();
            res.setId(review.getId());
            res.setUserId(review.getUserId());
            res.setVetId(review.getVetId());
            res.setRating(review.getRating());
            res.setComment(review.getComment());
            res.setCreatedAt(review.getCreatedAt());
            result.add(res);
        }
        return result;
    }
}
