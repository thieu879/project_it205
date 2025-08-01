package com.data.projectit205.service.review;

import com.data.projectit205.model.dto.request.ReviewRequestDTO;
import com.data.projectit205.model.entity.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getCourseReviews(Integer courseId);
    Review createReview(ReviewRequestDTO reviewRequestDTO, String username);
    Review updateReview(Integer reviewId, ReviewRequestDTO reviewRequestDTO, String username);
    void deleteReview(Integer reviewId, String username);
}
