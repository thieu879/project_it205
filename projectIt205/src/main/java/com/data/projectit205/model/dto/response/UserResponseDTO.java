package com.data.projectit205.model.dto.response;

import com.data.projectit205.model.entity.RoleName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {

    private Integer userId;
    private String username;
    private String email;
    private String fullName;
    private RoleName roleName;
    private Boolean isActive;
}
