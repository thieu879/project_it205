package com.data.projectit205.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestDTO {

    @NotNull
    private Integer userId;

    @NotBlank
    private String message;

    private String type;

    private String targetUrl;

    private Boolean isRead = false;
}
