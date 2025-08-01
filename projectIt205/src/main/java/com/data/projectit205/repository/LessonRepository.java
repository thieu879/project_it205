package com.data.projectit205.repository;

import com.data.projectit205.model.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findByCourseCourseIdAndIsPublishedOrderByOrderIndex(Integer courseId, Boolean isPublished);
    List<Lesson> findByCourseCourseIdOrderByOrderIndex(Integer courseId);

    @Query("SELECT l FROM Lesson l WHERE l.course.courseId = ?1 AND l.isPublished = true ORDER BY l.orderIndex")
    List<Lesson> findPublishedLessonsByCourse(Integer courseId);
}
