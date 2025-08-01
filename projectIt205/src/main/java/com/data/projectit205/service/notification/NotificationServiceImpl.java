package com.data.projectit205.service.notification;

import com.data.projectit205.model.dto.request.NotificationRequestDTO;
import com.data.projectit205.model.entity.Notification;
import com.data.projectit205.model.entity.User;
import com.data.projectit205.repository.NotificationRepository;
import com.data.projectit205.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Notification> getUserNotifications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
        return notificationRepository.findByUserUserIdOrderByCreatedAtDesc(user.getUserId());
    }

    @Override
    public Notification markAsRead(Integer notificationId, String username) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo!"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (!notification.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Bạn không có quyền đánh dấu thông báo này!");
        }

        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification createNotification(NotificationRequestDTO notificationRequestDTO) {
        User user = userRepository.findById(notificationRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(notificationRequestDTO.getMessage());
        notification.setType(notificationRequestDTO.getType());
        notification.setTargetUrl(notificationRequestDTO.getTargetUrl());
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Integer notificationId, String username) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo!"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (!notification.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Bạn không có quyền xóa thông báo này!");
        }

        notificationRepository.delete(notification);
    }
}
