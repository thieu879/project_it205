package com.data.projectit205.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class LessonRequestDTO {

    @NotNull
    private Integer courseId;

    @NotBlank
    private String title;

    private MultipartFile contentUrl;

    private String textContent;

    @NotNull
    private Integer orderIndex;

    private Boolean isPublished = false;
}
