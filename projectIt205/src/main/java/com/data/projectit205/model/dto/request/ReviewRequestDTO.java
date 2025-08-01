package com.data.projectit205.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {

    @NotNull
    private Integer courseId;

    @NotNull
    private Integer studentId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;
}

