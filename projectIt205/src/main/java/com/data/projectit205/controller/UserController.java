package com.data.projectit205.controller;

import com.data.projectit205.model.dto.request.UserCreateByAdminRequestDTO;
import com.data.projectit205.model.dto.request.UserLogin;
import com.data.projectit205.model.dto.request.UserRequestDTO;
import com.data.projectit205.model.dto.response.APIResponse;
import com.data.projectit205.model.dto.response.JWTResponse;
import com.data.projectit205.model.entity.User;
import com.data.projectit205.security.principal.CustomUserDetails;
import com.data.projectit205.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<User>> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO){
        User user = userService.registerStudent(userRequestDTO);
        return new ResponseEntity<>(new APIResponse<>(true,"Đăng ký thành công!", user, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @PostMapping("/register/teacher")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<User>> registerTeacher(@Valid @RequestBody UserRequestDTO userRequestDTO){
        User user = userService.registerTeacher(userRequestDTO);
        return new ResponseEntity<>(new APIResponse<>(true,"Tạo tài khoản giảng viên thành công!", user, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<JWTResponse>> login(@Valid @RequestBody UserLogin userLogin){
        JWTResponse response = userService.login(userLogin);
        return new ResponseEntity<>(new APIResponse<>(true,"Đăng nhập thành công!", response, HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<APIResponse<JWTResponse>> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        JWTResponse response = userService.logout(userDetails);
        return new ResponseEntity<>(new APIResponse<>(true, "Đăng xuất thành công!", response, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<APIResponse<User>> getProfile(Authentication authentication) {
        User user = userService.getUserProfile(authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true,"Lấy thông tin profile thành công!", user, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<User>>> getAllUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean isActive) {
        List<User> users = userService.getAllUsers(role, isActive);
        return new ResponseEntity<>(new APIResponse<>(true,"Lấy danh sách người dùng thành công!", users, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<User>> getUserById(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(new APIResponse<>(true,"Lấy thông tin người dùng thành công!", user, HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<User>> createUser(@Valid @RequestBody UserCreateByAdminRequestDTO userRequestDTO) {
        User user = userService.createUser(userRequestDTO);
        return new ResponseEntity<>(new APIResponse<>(true,"Tạo người dùng thành công!", user, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<User>> updateUserRole(@PathVariable Integer userId, @RequestParam String role) {
        User user = userService.updateUserRole(userId, role);
        return new ResponseEntity<>(new APIResponse<>(true,"Cập nhật vai trò thành công!", user, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<User>> updateUserStatus(@PathVariable Integer userId, @RequestParam Boolean isActive) {
        User user = userService.updateUserStatus(userId, isActive);
        return new ResponseEntity<>(new APIResponse<>(true,"Cập nhật trạng thái thành công!", user, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<APIResponse<User>> updateProfile(@Valid @RequestBody UserRequestDTO userRequestDTO, Authentication authentication) {
        User user = userService.updateProfile(authentication.getName(), userRequestDTO);
        return new ResponseEntity<>(new APIResponse<>(true,"Cập nhật thông tin cá nhân thành công!", user, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<APIResponse<String>> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            Authentication authentication) {
        userService.changePassword(authentication.getName(), oldPassword, newPassword);
        return new ResponseEntity<>(new APIResponse<>(true,"Đổi mật khẩu thành công!", "Password changed successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<String>> deleteUser(@PathVariable String username) {
        userService.deleteAccountStudent(username);
        return new ResponseEntity<>(new APIResponse<>(true,"Xóa người dùng thành công!", "User deleted successfully", HttpStatus.OK), HttpStatus.OK);
    }
}
