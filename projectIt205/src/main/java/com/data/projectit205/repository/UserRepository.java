package com.data.projectit205.repository;

import com.data.projectit205.model.entity.RoleName;
import com.data.projectit205.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByRoleRoleName(RoleName roleName);
    List<User> findByIsActive(Boolean isActive);
    List<User> findByRoleRoleNameAndIsActive(RoleName roleName, Boolean isActive);
}
