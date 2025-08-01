package com.data.projectit205.repository;

import com.data.projectit205.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserUserIdOrderByCreatedAtDesc(Integer userId);
    List<Notification> findByUserUserIdAndIsRead(Integer userId, Boolean isRead);
}
