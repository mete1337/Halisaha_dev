package com.halisaha.backend.controller;

import com.halisaha.backend.dto.ReviewCreateRequest;
import com.halisaha.backend.dto.ReviewResponse;
import com.halisaha.backend.service.Abstract.IReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse createReview(@RequestBody @Valid ReviewCreateRequest request, Principal principal) {
        try {
            return reviewService.createReview(request, principal.getName());
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponse> getMyReviews(Principal principal) {
        return reviewService.getMyReviews(principal.getName());
    }
}
