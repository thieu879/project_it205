package com.data.projectit205.repository;

import com.data.projectit205.model.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Integer> {
    Optional<LessonProgress> findByEnrollmentEnrollmentIdAndLessonLessonId(Integer enrollmentId, Integer lessonId);
    List<LessonProgress> findByEnrollmentEnrollmentId(Integer enrollmentId);
    List<LessonProgress> findByEnrollmentEnrollmentIdAndIsCompleted(Integer enrollmentId, Boolean isCompleted);
}
