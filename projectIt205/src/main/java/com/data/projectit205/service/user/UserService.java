package com.data.projectit205.service.user;

import com.data.projectit205.model.dto.request.UserCreateByAdminRequestDTO;
import com.data.projectit205.model.dto.request.UserLogin;
import com.data.projectit205.model.dto.request.UserRequestDTO;
import com.data.projectit205.model.dto.response.JWTResponse;
import com.data.projectit205.model.entity.User;
import com.data.projectit205.security.principal.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

public interface UserService {
    User registerStudent(UserRequestDTO userRequestDTO);
    User registerTeacher(UserRequestDTO userRequestDTO);
    JWTResponse login(UserLogin userLogin);
    User getUserProfile(String username);
    List<User> getAllUsers(String role, Boolean isActive);
    User getUserById(Integer userId);
    User createUser(UserCreateByAdminRequestDTO userRequestDTO);
    User updateUserRole(Integer userId, String role);
    User updateUserStatus(Integer userId, Boolean isActive);
    User updateProfile(String username, UserRequestDTO userRequestDTO);
    void changePassword(String username, String oldPassword, String newPassword);
    JWTResponse logout(@AuthenticationPrincipal CustomUserDetails customUserDetails);
    void deleteAccountStudent(String username);
}
