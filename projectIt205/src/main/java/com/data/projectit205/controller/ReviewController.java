package com.data.projectit205.controller;

import com.data.projectit205.model.dto.request.ReviewRequestDTO;
import com.data.projectit205.model.dto.response.APIResponse;
import com.data.projectit205.model.entity.Review;
import com.data.projectit205.service.review.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<APIResponse<List<Review>>> getCourseReviews(@PathVariable Integer courseId) {
        List<Review> reviews = reviewService.getCourseReviews(courseId);
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy danh sách đánh giá thành công!", reviews, HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<APIResponse<Review>> createReview(@Valid @RequestBody ReviewRequestDTO reviewRequestDTO, Authentication authentication) {
        Review review = reviewService.createReview(reviewRequestDTO, authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Tạo đánh giá thành công!", review, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @PutMapping("/{reviewId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<APIResponse<Review>> updateReview(@PathVariable Integer reviewId, @Valid @RequestBody ReviewRequestDTO reviewRequestDTO, Authentication authentication) {
        Review review = reviewService.updateReview(reviewId, reviewRequestDTO, authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Cập nhật đánh giá thành công!", review, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<APIResponse<String>> deleteReview(@PathVariable Integer reviewId, Authentication authentication) {
        reviewService.deleteReview(reviewId, authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Xóa đánh giá thành công!", "Review deleted successfully", HttpStatus.OK), HttpStatus.OK);
    }
}
