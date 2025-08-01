package com.data.projectit205.repository;

import com.data.projectit205.model.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByStudentUserId(Integer studentId);
    List<Enrollment> findByCourseCourseId(Integer courseId);
    Optional<Enrollment> findByStudentUserIdAndCourseCourseId(Integer studentId, Integer courseId);
}
