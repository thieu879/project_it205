package com.data.projectit205.service.course;

import com.data.projectit205.model.dto.request.CourseRequestDTO;
import com.data.projectit205.model.entity.Course;
import com.data.projectit205.model.entity.ECourseStatus;
import com.data.projectit205.model.entity.User;
import com.data.projectit205.repository.CourseRepository;
import com.data.projectit205.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Course> getAllCourses(String status, Integer teacherId) {
        if (status != null && teacherId != null) {
            ECourseStatus courseStatus = ECourseStatus.valueOf(status.toUpperCase());
            return courseRepository.findByStatusAndTeacherUserId(courseStatus, teacherId);
        } else if (status != null) {
            ECourseStatus courseStatus = ECourseStatus.valueOf(status.toUpperCase());
            return courseRepository.findByStatus(courseStatus);
        } else if (teacherId != null) {
            return courseRepository.findByTeacherUserId(teacherId);
        }
        return courseRepository.findByStatus(ECourseStatus.PUBLISHED);
    }

    @Override
    public Course getCourseById(Integer courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học!"));
    }

    @Override
    public Course createCourse(CourseRequestDTO courseRequestDTO, String username) {
        User teacher = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên!"));

        Course course = new Course();
        course.setTitle(courseRequestDTO.getTitle());
        course.setDescription(courseRequestDTO.getDescription());
        course.setTeacher(teacher);
        course.setPrice(courseRequestDTO.getPrice());
        course.setDurationHours(courseRequestDTO.getDurationHours());
        course.setStatus(ECourseStatus.DRAFT);
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());

        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Integer courseId, CourseRequestDTO courseRequestDTO, String username) {
        Course course = getCourseById(courseId);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (!course.getTeacher().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Bạn không có quyền cập nhật khóa học này!");
        }

        course.setTitle(courseRequestDTO.getTitle());
        course.setDescription(courseRequestDTO.getDescription());
        course.setPrice(courseRequestDTO.getPrice());
        course.setDurationHours(courseRequestDTO.getDurationHours());
        course.setUpdatedAt(LocalDateTime.now());

        return courseRepository.save(course);
    }

    @Override
    public Course updateCourseStatus(Integer courseId, String status, String username) {
        Course course = getCourseById(courseId);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (!course.getTeacher().getUserId().equals(user.getUserId()) &&
                !user.getRole().getRoleName().name().equals("ROLE_ADMIN")) {
            throw new RuntimeException("Bạn không có quyền cập nhật trạng thái khóa học này!");
        }

        ECourseStatus courseStatus = ECourseStatus.valueOf(status.toUpperCase());
        course.setStatus(courseStatus);
        course.setUpdatedAt(LocalDateTime.now());

        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Integer courseId, String username) {
        Course course = getCourseById(courseId);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (!course.getTeacher().getUserId().equals(user.getUserId()) &&
                !user.getRole().getRoleName().name().equals("ROLE_ADMIN")) {
            throw new RuntimeException("Bạn không có quyền xóa khóa học này!");
        }

        courseRepository.delete(course);
    }

    @Override
    public List<Course> searchCourses(String keyword) {
        return courseRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndStatus(
                keyword, keyword, ECourseStatus.PUBLISHED);
    }

    @Override
    public List<Course> getPopularCourses(Integer limit) {
        return courseRepository.findPopularCourses(limit);
    }

    @Override
    public List<Course> getCoursesByTeacher(String username) {
        User teacher = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên!"));
        return courseRepository.findByTeacherUserId(teacher.getUserId());
    }
}
