package com.halisaha.backend.controller;

import com.halisaha.backend.dto.ReviewCreateRequest;
import com.halisaha.backend.dto.ReviewResponse;
import com.halisaha.backend.service.Abstract.IReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user/review")
@RequiredArgsConstructor
public class ReviewController {

    private final IReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<ReviewResponse> createReview(@RequestBody @Valid ReviewCreateRequest request, Principal principal) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(reviewService.createReview(request, principal.getName()));
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReviewResponse>> getMyReviews(Principal principal) {
        return ResponseEntity.ok(reviewService.getMyReviews(principal.getName()));
    }
}
