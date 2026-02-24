package com.halisaha.backend.converter;

import com.halisaha.backend.dto.ReviewCreateRequest;
import com.halisaha.backend.dto.ReviewResponse;
import com.halisaha.backend.model.Pitch;
import com.halisaha.backend.model.Reservation;
import com.halisaha.backend.model.Review;
import com.halisaha.backend.model.User;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverter {

    public Review toModel(ReviewCreateRequest request, User user, Reservation reservation, Pitch pitch) {
        return Review.builder()
                .user(user)
                .reservation(reservation)
                .pitch(pitch)
                .rating(request.rating())
                .comment(request.comment())
                .build();
    }

    public ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getReservation().getId(),
                review.getPitch().getId(),
                review.getPitch().getName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}
