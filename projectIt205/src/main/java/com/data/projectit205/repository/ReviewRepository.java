package com.data.projectit205.repository;

import com.data.projectit205.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByCourseCourseIdOrderByCreatedAtDesc(Integer courseId);
    Optional<Review> findByCourseCourseIdAndStudentUserId(Integer courseId, Integer studentId);
    List<Review> findByStudentUserId(Integer studentId);
}
