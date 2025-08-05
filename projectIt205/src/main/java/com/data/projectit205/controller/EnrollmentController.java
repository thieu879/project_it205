package com.data.projectit205.controller;

import com.data.projectit205.model.dto.request.EnrollmentRequestDTO;
import com.data.projectit205.model.dto.response.APIResponse;
import com.data.projectit205.model.entity.Enrollment;
import com.data.projectit205.security.principal.CustomUserDetails;
import com.data.projectit205.service.enrollment.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/my-courses")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<APIResponse<List<Enrollment>>> getMyEnrollments(Authentication authentication) {
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy danh sách khóa học đã đăng ký thành công!", enrollments, HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping("/enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<APIResponse<Enrollment>> enrollCourse(@RequestBody EnrollmentRequestDTO enrollmentRequestDTO, Authentication authentication) {
        Enrollment enrollment = enrollmentService.enrollCourse(enrollmentRequestDTO.getCourseId(), authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Đăng ký khóa học thành công!", enrollment, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/{enrollmentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<APIResponse<Enrollment>> getEnrollmentDetail(@PathVariable Integer enrollmentId, Authentication authentication) {
        Enrollment enrollment = enrollmentService.getEnrollmentDetail(enrollmentId, authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy thông tin đăng ký thành công!", enrollment, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/{enrollmentId}/progress")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<APIResponse<Enrollment>> updateProgress(@PathVariable Integer enrollmentId, @RequestParam Integer lessonId, Authentication authentication) {
        Enrollment enrollment = enrollmentService.updateLessonProgress(enrollmentId, lessonId, authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Cập nhật tiến độ học thành công!", enrollment, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/statistics/{studentId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Map<String, Object>>> getStudentStatistics(@PathVariable Integer studentId) {
        Map<String, Object> statistics = enrollmentService.getStudentStatistics(studentId);
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy thống kê sinh viên thành công!", statistics, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/teacher-statistics")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<APIResponse<Map<String, Object>>> getTeacherStatistics(Authentication authentication) {
        Map<String, Object> statistics = enrollmentService.getTeacherStatistics(authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy thống kê giảng viên thành công!", statistics, HttpStatus.OK), HttpStatus.OK);
    }
}
