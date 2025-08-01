package com.data.projectit205.security.config;

import com.data.projectit205.model.entity.Role;
import com.data.projectit205.model.entity.RoleName;
import com.data.projectit205.model.entity.User;
import com.data.projectit205.repository.RoleRepository;
import com.data.projectit205.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = createRoleIfNotExists(RoleName.ROLE_ADMIN);
        Role teacherRole = createRoleIfNotExists(RoleName.ROLE_TEACHER);
        Role studentRole = createRoleIfNotExists(RoleName.ROLE_STUDENT);

        // Tạo admin user mặc định
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .passwordHash(passwordEncoder.encode("Admin123"))
                    .email("admin@example.com")
                    .fullName("System Administrator")
                    .role(adminRole)
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userRepository.save(admin);
            System.out.println("Tạo admin user thành công: admin/admin123");
        }

        System.out.println("Khởi tạo dữ liệu ban đầu thành công!");
    }
    
    private Role createRoleIfNotExists(RoleName roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setRoleName(roleName);
                    return roleRepository.save(role);
                });
    }
}

