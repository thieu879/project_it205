package com.data.projectit205.service.enrollment;

import com.data.projectit205.model.entity.Enrollment;

import java.util.List;
import java.util.Map;

public interface EnrollmentService {
    List<Enrollment> getStudentEnrollments(String username);
    Enrollment enrollCourse(Integer courseId, String username);
    Enrollment getEnrollmentDetail(Integer enrollmentId, String username);
    Enrollment updateLessonProgress(Integer enrollmentId, Integer lessonId, String username);
    Map<String, Object> getStudentStatistics(Integer studentId);
    Map<String, Object> getTeacherStatistics(String username);
}
