package com.data.projectit205.model.dto.request;

import com.data.projectit205.model.entity.ECourseStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CourseRequestDTO {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Integer teacherId;

    @NotNull
    private BigDecimal price = BigDecimal.ZERO;

    private Integer durationHours;

    private ECourseStatus status = ECourseStatus.DRAFT;
}
