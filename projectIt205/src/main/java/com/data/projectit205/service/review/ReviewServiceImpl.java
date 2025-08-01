package com.data.projectit205.service.review;

import com.data.projectit205.model.dto.request.ReviewRequestDTO;
import com.data.projectit205.model.entity.Course;
import com.data.projectit205.model.entity.Review;
import com.data.projectit205.model.entity.User;
import com.data.projectit205.repository.CourseRepository;
import com.data.projectit205.repository.EnrollmentRepository;
import com.data.projectit205.repository.ReviewRepository;
import com.data.projectit205.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public List<Review> getCourseReviews(Integer courseId) {
        return reviewRepository.findByCourseCourseIdOrderByCreatedAtDesc(courseId);
    }

    @Override
    public Review createReview(ReviewRequestDTO reviewRequestDTO, String username) {
        Course course = courseRepository.findById(reviewRequestDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học!"));

        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên!"));

        // Kiểm tra sinh viên đã đăng ký khóa học chưa
        if (enrollmentRepository.findByStudentUserIdAndCourseCourseId(student.getUserId(), course.getCourseId()).isEmpty()) {
            throw new RuntimeException("Bạn phải đăng ký khóa học trước khi đánh giá!");
        }

        // Kiểm tra đã đánh giá chưa - SỬA TẠI ĐÂY
        if (reviewRepository.findByCourseCourseIdAndStudentUserId(course.getCourseId(), student.getUserId()).isPresent()) {
            throw new RuntimeException("Bạn đã đánh giá khóa học này rồi!");
        }

        Review review = new Review();
        review.setCourse(course);
        review.setStudent(student);
        review.setRating(reviewRequestDTO.getRating());
        review.setComment(reviewRequestDTO.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    @Override
    public Review updateReview(Integer reviewId, ReviewRequestDTO reviewRequestDTO, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá!"));

        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên!"));

        if (!review.getStudent().getUserId().equals(student.getUserId())) {
            throw new RuntimeException("Bạn không có quyền cập nhật đánh giá này!");
        }

        review.setRating(reviewRequestDTO.getRating());
        review.setComment(reviewRequestDTO.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Integer reviewId, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá!"));

        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên!"));

        if (!review.getStudent().getUserId().equals(student.getUserId())) {
            throw new RuntimeException("Bạn không có quyền xóa đánh giá này!");
        }

        reviewRepository.delete(review);
    }
}
