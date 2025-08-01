package com.data.projectit205.service.user;

import com.data.projectit205.model.dto.request.UserCreateByAdminRequestDTO;
import com.data.projectit205.model.dto.request.UserLogin;
import com.data.projectit205.model.dto.request.UserRequestDTO;
import com.data.projectit205.model.dto.response.JWTResponse;
import com.data.projectit205.model.entity.Role;
import com.data.projectit205.model.entity.RoleName;
import com.data.projectit205.model.entity.User;
import com.data.projectit205.repository.RoleRepository;
import com.data.projectit205.repository.UserRepository;
import com.data.projectit205.security.jwt.JWTProvider;
import com.data.projectit205.security.principal.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User registerStudent(UserRequestDTO userRequestDTO) {
        if (userRepository.findByUsername(userRequestDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username đã tồn tại!");
        }

        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        Role role = mapRoleStringToRole(RoleName.ROLE_STUDENT);

        User user = User.builder()
                .username(userRequestDTO.getUsername())
                .passwordHash(passwordEncoder.encode(userRequestDTO.getPassword()))
                .email(userRequestDTO.getEmail())
                .fullName(userRequestDTO.getFullName())
                .isActive(false)
                .role(role)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return userRepository.save(user);
    }


    @Override
    public User registerTeacher(UserRequestDTO userRequestDTO) {
        if (userRepository.findByUsername(userRequestDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username đã tồn tại!");
        }

        Role role = mapRoleStringToRole(RoleName.ROLE_TEACHER);

        User user = User.builder()
                .username(userRequestDTO.getUsername())
                .passwordHash(passwordEncoder.encode(userRequestDTO.getPassword()))
                .email(userRequestDTO.getEmail())
                .fullName(userRequestDTO.getFullName())
                .isActive(false)
                .role(role)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    @Override
    public JWTResponse login(UserLogin userLogin) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword())
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtProvider.generateToken(userDetails.getUsername());

            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setIsActive(true);
            userRepository.save(user);

            return JWTResponse.builder()
                    .username(userDetails.getUsername())
                    .fullName(userDetails.getUser().getFullName())
                    .email(userDetails.getUser().getEmail())
                    .enabled(userDetails.isEnabled())
                    .authorities(userDetails.getAuthorities())
                    .isActive(userDetails.getUser().getIsActive())
                    .token(token)
                    .build();
        } catch (AuthenticationException e) {
            throw new RuntimeException("Sai username hoặc password!");
        }
    }

    @Override
    public User getUserProfile(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
    }

    @Override
    public List<User> getAllUsers(String role, Boolean isActive) {
        if (role != null && isActive != null) {
            RoleName roleName = RoleName.valueOf("ROLE_" + role.toUpperCase());
            return userRepository.findByRoleRoleNameAndIsActive(roleName, isActive);
        } else if (role != null) {
            RoleName roleName = RoleName.valueOf("ROLE_" + role.toUpperCase());
            return userRepository.findByRoleRoleName(roleName);
        } else if (isActive != null) {
            return userRepository.findByIsActive(isActive);
        }
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
    }

    @Override
    public User createUser(UserCreateByAdminRequestDTO userRequestDTO) {
        if (userRepository.findByUsername(userRequestDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username đã tồn tại!");
        }

        Role role = mapRoleStringToRole(userRequestDTO.getRole());

        User user = User.builder()
                .username(userRequestDTO.getUsername())
                .passwordHash(passwordEncoder.encode(userRequestDTO.getPassword()))
                .email(userRequestDTO.getEmail())
                .fullName(userRequestDTO.getFullName())
                .isActive(userRequestDTO.getIsActive())
                .role(role)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return userRepository.save(user);
    }

    @Override
    public User updateUserRole(Integer userId, String role) {
        User user = getUserById(userId);
        RoleName roleName = RoleName.valueOf("ROLE_" + role.toUpperCase());
        Role newRole = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role không tồn tại!"));

        user.setRole(newRole);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User updateUserStatus(Integer userId, Boolean isActive) {
        User user = getUserById(userId);
        user.setIsActive(isActive);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User updateProfile(String username, UserRequestDTO userRequestDTO) {
        User user = getUserProfile(username);
        user.setEmail(userRequestDTO.getEmail());
        user.setFullName(userRequestDTO.getFullName());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = getUserProfile(username);
        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new RuntimeException("Mật khẩu cũ không đúng!");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public JWTResponse logout(CustomUserDetails customUserDetails) {
        User user = userRepository.findByUsername(customUserDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
        user.setIsActive(false);
        userRepository.save(user);
        return JWTResponse.builder()
                .isActive(false)
                .token(null)
                .username(null)
                .fullName(null)
                .email(null)
                .enabled(false)
                .authorities(null)
                .build();
    }

    // chỉ có admin mới có thể xóa tài khoản học sinh
    @Override
    public void deleteAccountStudent(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (!user.getRole().getRoleName().equals(RoleName.ROLE_STUDENT)) {
            throw new RuntimeException("Chỉ có thể xóa tài khoản học sinh!");
        }

        userRepository.delete(user);
    }

    private Role mapRoleStringToRole(RoleName roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new NoSuchElementException("Không tồn tại role: " + roleName));
    }
}
