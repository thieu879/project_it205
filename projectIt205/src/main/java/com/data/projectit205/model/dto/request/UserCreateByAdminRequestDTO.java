package com.data.projectit205.model.dto.request;

import com.data.projectit205.model.entity.RoleName;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateByAdminRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String fullName;

    @NotNull
    private RoleName role;

    private Boolean isActive = true;
}


