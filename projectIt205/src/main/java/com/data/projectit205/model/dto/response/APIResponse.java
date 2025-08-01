package com.data.projectit205.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class APIResponse <T>{
    private Boolean success;
    private String message;
    private T data;
    private HttpStatus status;
}
