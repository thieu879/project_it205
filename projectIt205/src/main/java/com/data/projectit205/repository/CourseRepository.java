package com.data.projectit205.repository;

import com.data.projectit205.model.entity.Course;
import com.data.projectit205.model.entity.ECourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByStatus(ECourseStatus status);
    List<Course> findByTeacherUserId(Integer teacherId);
    List<Course> findByStatusAndTeacherUserId(ECourseStatus status, Integer teacherId);

    List<Course> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndStatus(
            String title, String description, ECourseStatus status);

    @Query("SELECT c FROM Course c LEFT JOIN Enrollment e ON c.courseId = e.course.courseId " +
            "WHERE c.status = 'PUBLISHED' " +
            "GROUP BY c.courseId " +
            "ORDER BY COUNT(e.enrollmentId) DESC")
    List<Course> findPopularCourses(@Param("limit") Integer limit);
}
