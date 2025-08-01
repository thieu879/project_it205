package com.data.projectit205.controller;

import com.data.projectit205.model.dto.request.NotificationRequestDTO;
import com.data.projectit205.model.dto.response.APIResponse;
import com.data.projectit205.model.entity.Notification;
import com.data.projectit205.service.notification.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<APIResponse<List<Notification>>> getMyNotifications(Authentication authentication) {
        List<Notification> notifications = notificationService.getUserNotifications(authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy danh sách thông báo thành công!", notifications, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<APIResponse<Notification>> markAsRead(@PathVariable Integer notificationId, Authentication authentication) {
        Notification notification = notificationService.markAsRead(notificationId, authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Đánh dấu đã đọc thành công!", notification, HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIResponse<Notification>> createNotification(@Valid @RequestBody NotificationRequestDTO notificationRequestDTO) {
        Notification notification = notificationService.createNotification(notificationRequestDTO);
        return new ResponseEntity<>(new APIResponse<>(true, "Tạo thông báo thành công!", notification, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<APIResponse<String>> deleteNotification(@PathVariable Integer notificationId, Authentication authentication) {
        notificationService.deleteNotification(notificationId, authentication.getName());
        return new ResponseEntity<>(new APIResponse<>(true, "Xóa thông báo thành công!", "Notification deleted successfully", HttpStatus.OK), HttpStatus.OK);
    }
}
