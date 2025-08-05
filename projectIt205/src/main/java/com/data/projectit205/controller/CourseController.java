package com.data.projectit205.controller;

import com.data.projectit205.model.dto.request.CourseRequestDTO;
import com.data.projectit205.model.dto.response.APIResponse;
import com.data.projectit205.model.entity.Course;
import com.data.projectit205.service.course.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<APIResponse<List<Course>>> getAllCourses(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer teacherId) {
        List<Course> courses = courseService.getAllCourses(status, teacherId);
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy danh sách khóa học thành công!", courses, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<APIResponse<Course>> getCourseById(@PathVariable Integer courseId) {
        Course course = courseService.getCourseById(courseId);
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy thông tin khóa học thành công!", course, HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<APIResponse<Course>> createCourse(@Valid @RequestBody CourseRequestDTO courseRequestDTO, Authentication authentication) {
        Course course = courseService.createCourse(courseRequestDTO, authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Tạo khóa học thành công!", course, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @PutMapping("/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<APIResponse<Course>> updateCourse(@PathVariable Integer courseId, @Valid @RequestBody CourseRequestDTO courseRequestDTO, Authentication authentication) {
        Course course = courseService.updateCourse(courseId, courseRequestDTO, authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Cập nhật khóa học thành công!", course, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/{courseId}/status")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Course>> updateCourseStatus(@PathVariable Integer courseId, @RequestParam String status, Authentication authentication) {
        Course course = courseService.updateCourseStatus(courseId, status, authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Cập nhật trạng thái khóa học thành công!", course, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<String>> deleteCourse(@PathVariable Integer courseId, Authentication authentication) {
        courseService.deleteCourse(courseId, authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Xóa khóa học thành công!", "Course deleted successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<APIResponse<List<Course>>> searchCourses(@PathVariable String keyword) {
        List<Course> courses = courseService.searchCourses(keyword);
        return new ResponseEntity<>(new APIResponse<>(true, "Tìm kiếm khóa học thành công!", courses, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/popular")
    public ResponseEntity<APIResponse<List<Course>>> getPopularCourses(@RequestParam(defaultValue = "10") Integer limit) {
        List<Course> courses = courseService.getPopularCourses(limit);
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy danh sách khóa học phổ biến thành công!", courses, HttpStatus.OK), HttpStatus.OK);
    }
}
