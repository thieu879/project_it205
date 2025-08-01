package com.data.projectit205.service.notification;

import com.data.projectit205.model.dto.request.NotificationRequestDTO;
import com.data.projectit205.model.entity.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> getUserNotifications(String username);
    Notification markAsRead(Integer notificationId, String username);
    Notification createNotification(NotificationRequestDTO notificationRequestDTO);
    void deleteNotification(Integer notificationId, String username);
}
