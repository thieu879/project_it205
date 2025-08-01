package com.data.projectit205.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLogin {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
