package com.halisaha.backend.service.Abstract;

import com.halisaha.backend.dto.ReviewCreateRequest;
import com.halisaha.backend.dto.ReviewResponse;

import java.util.List;

public interface IReviewService {
    ReviewResponse createReview(ReviewCreateRequest request, String username);
    List<ReviewResponse> getMyReviews(String username);
}
