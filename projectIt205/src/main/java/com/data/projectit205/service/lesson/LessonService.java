package com.data.projectit205.service.lesson;

import com.data.projectit205.model.dto.request.LessonRequestDTO;
import com.data.projectit205.model.entity.Lesson;

import java.io.IOException;
import java.util.List;

public interface LessonService {
    List<Lesson> getLessonsByCourse(Integer courseId);
    Lesson getLessonById(Integer lessonId);
    Lesson createLesson(LessonRequestDTO lessonRequestDTO, String username) throws IOException;
    Lesson updateLesson(Integer lessonId, LessonRequestDTO lessonRequestDTO, String username);
    Lesson updateLessonPublishStatus(Integer lessonId, Boolean isPublished, String username);
    void deleteLesson(Integer lessonId, String username);
    String getLessonPreview(Integer lessonId);
}
