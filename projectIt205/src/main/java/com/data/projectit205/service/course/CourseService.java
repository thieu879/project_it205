package com.data.projectit205.service.course;

import com.data.projectit205.model.dto.request.CourseRequestDTO;
import com.data.projectit205.model.entity.Course;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses(String status, Integer teacherId);
    Course getCourseById(Integer courseId);
    Course createCourse(CourseRequestDTO courseRequestDTO, String username);
    Course updateCourse(Integer courseId, CourseRequestDTO courseRequestDTO, String username);
    Course updateCourseStatus(Integer courseId, String status, String username);
    void deleteCourse(Integer courseId, String username);
    List<Course> searchCourses(String keyword);
    List<Course> getPopularCourses(Integer limit);
    List<Course> getCoursesByTeacher(String username);
}
