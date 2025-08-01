package com.data.projectit205.model.dto.request;

import com.data.projectit205.model.entity.EEnrollmentStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EnrollmentRequestDTO {

    @NotNull
    private Integer studentId;

    @NotNull
    private Integer courseId;

    private EEnrollmentStatus status = EEnrollmentStatus.ENROLLED;

    private BigDecimal progressPercentage = BigDecimal.ZERO;
}
