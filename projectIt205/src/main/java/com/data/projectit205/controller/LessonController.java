package com.data.projectit205.controller;

import com.data.projectit205.model.dto.request.LessonRequestDTO;
import com.data.projectit205.model.dto.response.APIResponse;
import com.data.projectit205.model.entity.Lesson;
import com.data.projectit205.service.lesson.LessonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<APIResponse<List<Lesson>>> getLessonsByCourse(@PathVariable Integer courseId) {
        List<Lesson> lessons = lessonService.getLessonsByCourse(courseId);
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy danh sách bài học thành công!", lessons, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<APIResponse<Lesson>> getLessonById(@PathVariable Integer lessonId) {
        Lesson lesson = lessonService.getLessonById(lessonId);
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy thông tin bài học thành công!", lesson, HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<APIResponse<Lesson>> createLesson(
            @ModelAttribute @Valid LessonRequestDTO lessonRequestDTO,
            Authentication authentication) {
        try {
            Lesson lesson = lessonService.createLesson(lessonRequestDTO, authentication.getName());
            return new ResponseEntity<>(new APIResponse<>(true, "Tạo bài học thành công!", lesson, HttpStatus.CREATED), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new APIResponse<>(false, e.getMessage(), null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{lessonId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<APIResponse<Lesson>> updateLesson(
            @PathVariable Integer lessonId,
            @ModelAttribute @Valid LessonRequestDTO lessonRequestDTO,
            Authentication authentication) {
        try {
            Lesson lesson = lessonService.updateLesson(lessonId, lessonRequestDTO, authentication.getName());
            return new ResponseEntity<>(new APIResponse<>(true, "Cập nhật bài học thành công!", lesson, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new APIResponse<>(false, e.getMessage(), null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{lessonId}/publish")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<APIResponse<Lesson>> updateLessonPublishStatus(@PathVariable Integer lessonId, @RequestParam Boolean isPublished, Authentication authentication) {
        Lesson lesson = lessonService.updateLessonPublishStatus(lessonId, isPublished, authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Cập nhật trạng thái hiển thị bài học thành công!", lesson, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/{lessonId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<APIResponse<String>> deleteLesson(@PathVariable Integer lessonId, Authentication authentication) {
        lessonService.deleteLesson(lessonId, authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Xóa bài học thành công!", "Lesson deleted successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/{lessonId}/preview")
    public ResponseEntity<APIResponse<String>> getLessonPreview(@PathVariable Integer lessonId) {
        String preview = lessonService.getLessonPreview(lessonId);
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy nội dung xem trước thành công!", preview, HttpStatus.OK), HttpStatus.OK);
    }
}
