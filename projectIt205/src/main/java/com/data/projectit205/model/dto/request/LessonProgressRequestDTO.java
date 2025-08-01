package com.data.projectit205.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LessonProgressRequestDTO {
    @NotNull
    private Integer enrollmentId;

    @NotNull
    private Integer lessonId;

    private Boolean isCompleted = false;

    private LocalDateTime completedAt;

    private LocalDateTime lastAccessedAt;
}

