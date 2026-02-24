package com.halisaha.backend.service;

import com.halisaha.backend.dto.ReviewCreateRequest;
import com.halisaha.backend.dto.ReviewResponse;
import com.halisaha.backend.model.Reservation;
import com.halisaha.backend.model.Review;
import com.halisaha.backend.model.User;
import com.halisaha.backend.repository.ReservationRepository;
import com.halisaha.backend.repository.ReviewRepository;
import com.halisaha.backend.repository.UserRepository;
import com.halisaha.backend.service.Abstract.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewResponse createReview(ReviewCreateRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only review your own reservation");
        }

        LocalDateTime reservationEnd = LocalDateTime.of(reservation.getBookingDate(), reservation.getEndTime());
        if (LocalDateTime.now().isBefore(reservationEnd)) {
            throw new RuntimeException("You can review only after match time ends");
        }

        if (reviewRepository.existsByReservationId(reservation.getId())) {
            throw new RuntimeException("This reservation is already reviewed");
        }

        Review review = Review.builder()
                .user(user)
                .pitch(reservation.getSubPitch().getPitch())
                .reservation(reservation)
                .rating(request.rating())
                .comment(request.comment())
                .build();

        Review saved = reviewRepository.save(review);

        return new ReviewResponse(
                saved.getId(),
                saved.getReservation().getId(),
                saved.getPitch().getId(),
                saved.getPitch().getName(),
                saved.getRating(),
                saved.getComment(),
                saved.getCreatedAt()
        );
    }

    @Override
    public List<ReviewResponse> getMyReviews(String username) {
        return reviewRepository.findAllByUser_Username(username).stream()
                .map(r -> new ReviewResponse(
                        r.getId(),
                        r.getReservation().getId(),
                        r.getPitch().getId(),
                        r.getPitch().getName(),
                        r.getRating(),
                        r.getComment(),
                        r.getCreatedAt()
                ))
                .toList();
    }
}
