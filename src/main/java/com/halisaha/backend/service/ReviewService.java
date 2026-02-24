package com.halisaha.backend.service;

import com.halisaha.backend.converter.ReviewConverter;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ReviewConverter reviewConverter;

    @Override
    public ReviewResponse createReview(ReviewCreateRequest request, String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Reservation reservation = reservationRepository.findById(request.reservationId())
                    .orElseThrow(() -> new RuntimeException("Reservation not found"));

            if (!reservation.getUser().getId().equals(user.getId())) {
                log.warn("Review rejected: user {} not owner of reservation {}", username, request.reservationId());
                throw new RuntimeException("You can only review your own reservation");
            }

            LocalDateTime reservationEnd = LocalDateTime.of(reservation.getBookingDate(), reservation.getEndTime());
            if (LocalDateTime.now().isBefore(reservationEnd)) {
                log.warn("Review rejected: reservation {} not ended yet for user {}", request.reservationId(), username);
                throw new RuntimeException("You can review only after match time ends");
            }

            if (reviewRepository.existsByReservationId(reservation.getId())) {
                log.warn("Review rejected: reservation {} already reviewed", request.reservationId());
                throw new RuntimeException("This reservation is already reviewed");
            }

            Review saved = reviewRepository.save(
                    reviewConverter.toModel(request, user, reservation, reservation.getSubPitch().getPitch())
            );
            log.info("Review {} created by user {}", saved.getId(), username);
            return reviewConverter.toResponse(saved);
        } catch (RuntimeException ex) {
            log.warn("Review create failed for user {}: {}", username, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while creating review for user {}", username, ex);
            throw ex;
        }
    }

    @Override
    public List<ReviewResponse> getMyReviews(String username) {
        try {
            List<ReviewResponse> reviews = reviewRepository.findAllByUser_Username(username).stream()
                    .map(reviewConverter::toResponse)
                    .toList();
            log.info("Reviews listed for user {}", username);
            return reviews;
        } catch (RuntimeException ex) {
            log.warn("Review list failed for user {}: {}", username, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while listing reviews for user {}", username, ex);
            throw ex;
        }
    }
}
