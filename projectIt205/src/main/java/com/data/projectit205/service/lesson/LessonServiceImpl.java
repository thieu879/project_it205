package com.data.projectit205.service.lesson;

import com.data.projectit205.model.dto.request.LessonRequestDTO;
import com.data.projectit205.model.entity.Course;
import com.data.projectit205.model.entity.Lesson;
import com.data.projectit205.model.entity.User;
import com.data.projectit205.repository.CourseRepository;
import com.data.projectit205.repository.LessonRepository;
import com.data.projectit205.repository.UserRepository;
import com.data.projectit205.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class LessonServiceImpl implements LessonService {
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public List<Lesson> getLessonsByCourse(Integer courseId) {
        return lessonRepository.findByCourseCourseIdAndIsPublishedOrderByOrderIndex(courseId, true);
    }

    @Override
    public Lesson getLessonById(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài học!"));

        if (!lesson.getIsPublished()) {
            throw new RuntimeException("Bài học chưa được công bố!");
        }

        return lesson;
    }

    @Override
    public Lesson createLesson(LessonRequestDTO lessonRequestDTO, String username) {
        Course course = courseRepository.findById(lessonRequestDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học!"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (!course.getTeacher().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Bạn không có quyền tạo bài học cho khóa học này!");
        }

        String contentUrl = null;
        if (lessonRequestDTO.getContentUrl() != null && !lessonRequestDTO.getContentUrl().isEmpty()) {
            try {
                contentUrl = cloudinaryService.uploadFile(lessonRequestDTO.getContentUrl());
            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi upload file: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("File không hợp lệ: " + e.getMessage());
            }
        }

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setTitle(lessonRequestDTO.getTitle());
        lesson.setContentUrl(contentUrl);
        lesson.setTextContent(lessonRequestDTO.getTextContent());
        lesson.setOrderIndex(lessonRequestDTO.getOrderIndex());
        lesson.setIsPublished(lessonRequestDTO.getIsPublished());
        lesson.setCreatedAt(LocalDateTime.now());
        lesson.setUpdatedAt(LocalDateTime.now());

        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson updateLesson(Integer lessonId, LessonRequestDTO lessonRequestDTO, String username) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài học!"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (!lesson.getCourse().getTeacher().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Bạn không có quyền cập nhật bài học này!");
        }

        if (lessonRequestDTO.getContentUrl() != null && !lessonRequestDTO.getContentUrl().isEmpty()) {
            try {
                String newContentUrl = cloudinaryService.uploadFile(lessonRequestDTO.getContentUrl());
                lesson.setContentUrl(newContentUrl);
            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi upload file: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("File không hợp lệ: " + e.getMessage());
            }
        }

        lesson.setTitle(lessonRequestDTO.getTitle());
        lesson.setTextContent(lessonRequestDTO.getTextContent());
        lesson.setOrderIndex(lessonRequestDTO.getOrderIndex());
        lesson.setUpdatedAt(LocalDateTime.now());

        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson updateLessonPublishStatus(Integer lessonId, Boolean isPublished, String username) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài học!"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (!lesson.getCourse().getTeacher().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Bạn không có quyền cập nhật trạng thái bài học này!");
        }

        lesson.setIsPublished(isPublished);
        lesson.setUpdatedAt(LocalDateTime.now());

        return lessonRepository.save(lesson);
    }

    @Override
    public void deleteLesson(Integer lessonId, String username) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài học!"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (!lesson.getCourse().getTeacher().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Bạn không có quyền xóa bài học này!");
        }

        lessonRepository.delete(lesson);
    }

    @Override
    public String getLessonPreview(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài học!"));

        if (lesson.getTextContent() != null) {
            return lesson.getTextContent().length() > 200 ?
                    lesson.getTextContent().substring(0, 200) + "..." :
                    lesson.getTextContent();
        }

        return "Xem trước không khả dụng";
    }
}
