package com.data.projectit205.service.enrollment;

import com.data.projectit205.model.entity.*;
import com.data.projectit205.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private LessonProgressRepository lessonProgressRepository;

    @Override
    public List<Enrollment> getStudentEnrollments(String username) {
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên!"));
        return enrollmentRepository.findByStudentUserId(student.getUserId());
    }

    @Override
    public Enrollment enrollCourse(Integer courseId, String username) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học!"));

        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên!"));

        // Kiểm tra đã đăng ký chưa
        if (enrollmentRepository.findByStudentUserIdAndCourseCourseId(student.getUserId(), courseId).isPresent()) {
            throw new RuntimeException("Bạn đã đăng ký khóa học này rồi!");
        }

        // Kiểm tra khóa học có được công bố không
        if (!course.getStatus().equals(ECourseStatus.PUBLISHED)) {
            throw new RuntimeException("Khóa học chưa được công bố!");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setStatus(EEnrollmentStatus.ENROLLED);
        enrollment.setProgressPercentage(BigDecimal.ZERO);

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment getEnrollmentDetail(Integer enrollmentId, String username) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin đăng ký!"));

        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên!"));

        if (!enrollment.getStudent().getUserId().equals(student.getUserId())) {
            throw new RuntimeException("Bạn không có quyền xem thông tin đăng ký này!");
        }

        return enrollment;
    }

    @Override
    public Enrollment updateLessonProgress(Integer enrollmentId, Integer lessonId, String username) {
        Enrollment enrollment = getEnrollmentDetail(enrollmentId, username);

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài học!"));

        // Kiểm tra bài học thuộc khóa học đã đăng ký
        if (!lesson.getCourse().getCourseId().equals(enrollment.getCourse().getCourseId())) {
            throw new RuntimeException("Bài học không thuộc khóa học đã đăng ký!");
        }

        // Tạo hoặc cập nhật tiến độ bài học - SỬA TẠI ĐÂY
        LessonProgress progress = lessonProgressRepository
                .findByEnrollmentEnrollmentIdAndLessonLessonId(enrollmentId, lessonId)
                .orElse(new LessonProgress());

        progress.setEnrollment(enrollment);
        progress.setLesson(lesson);
        progress.setIsCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());
        progress.setLastAccessedAt(LocalDateTime.now());

        lessonProgressRepository.save(progress);

        // Cập nhật tiến độ tổng thể
        updateEnrollmentProgress(enrollment);

        return enrollmentRepository.save(enrollment);
    }

    private void updateEnrollmentProgress(Enrollment enrollment) {
        List<Lesson> allLessons = lessonRepository.findPublishedLessonsByCourse(enrollment.getCourse().getCourseId());

        List<LessonProgress> completedLessons = lessonProgressRepository
                .findByEnrollmentEnrollmentIdAndIsCompleted(enrollment.getEnrollmentId(), true);

        if (!allLessons.isEmpty()) {
            BigDecimal progress = BigDecimal.valueOf((double) completedLessons.size() / allLessons.size() * 100);
            enrollment.setProgressPercentage(progress);

            // Nếu hoàn thành 100% thì cập nhật trạng thái
            if (progress.compareTo(BigDecimal.valueOf(100)) == 0) {
                enrollment.setStatus(EEnrollmentStatus.COMPLETED);
                enrollment.setCompletionDate(LocalDateTime.now());
            }
        }
    }

    @Override
    public Map<String, Object> getStudentStatistics(Integer studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên!"));

        List<Enrollment> enrollments = enrollmentRepository.findByStudentUserId(studentId);

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalEnrolledCourses", enrollments.size());
        statistics.put("completedCourses", enrollments.stream()
                .filter(e -> e.getStatus().equals(EEnrollmentStatus.COMPLETED)).count());
        statistics.put("inProgressCourses", enrollments.stream()
                .filter(e -> e.getStatus().equals(EEnrollmentStatus.ENROLLED)).count());
        statistics.put("averageProgress", enrollments.stream()
                .mapToDouble(e -> e.getProgressPercentage().doubleValue())
                .average().orElse(0.0));

        return statistics;
    }

    @Override
    public Map<String, Object> getTeacherStatistics(String username) {
        User teacher = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên!"));

        List<Course> courses = courseRepository.findByTeacherUserId(teacher.getUserId());

        int totalEnrollments = courses.stream()
                .mapToInt(course -> enrollmentRepository.findByCourseCourseId(course.getCourseId()).size())
                .sum();

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalCourses", courses.size());
        statistics.put("publishedCourses", courses.stream()
                .filter(c -> c.getStatus().equals(ECourseStatus.PUBLISHED)).count());
        statistics.put("totalStudents", totalEnrollments);
        statistics.put("courses", courses);

        return statistics;
    }
}
