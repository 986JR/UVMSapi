package com.uvms.apiuvms.controller;

import com.uvms.apiuvms.entity.Notifications;
import com.uvms.apiuvms.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<Notifications> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/unread")
    public List<Notifications> getUnreadNotifications() {
        return notificationService.getUnreadNotifications();
    }

    @GetMapping("/{notification_id}")
    public ResponseEntity<Notifications> getNotificationById(@PathVariable Integer notification_id) {
        Optional<Notifications> notification = notificationService.getNotificationById(notification_id);
        return notification.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Notifications createNotification(@RequestBody Notifications notification) {
        return notificationService.saveNotification(notification);
    }

    @PutMapping("/{notification_id}")
    public ResponseEntity<Notifications> updateNotification(@PathVariable Integer notification_id,
                                                            @RequestBody Notifications notificationDetails) {
        Optional<Notifications> notificationOptional = notificationService.getNotificationById(notification_id);

        if (notificationOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Notifications notification = notificationOptional.get();
        notification.setTitle(notificationDetails.getTitle());
        notification.setMessage(notificationDetails.getMessage());
        notification.setIsRead(notificationDetails.getIsRead());
        notification.setRelatedEntity(notificationDetails.getRelatedEntity());
        notification.setRelatedEntityId(notificationDetails.getRelatedEntityId());

        Notifications updatedNotification = notificationService.saveNotification(notification);
        return ResponseEntity.ok(updatedNotification);
    }

    @PatchMapping("/{notification_id}/read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Integer notification_id) {
        notificationService.markAsRead(notification_id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{notification_id}")
    public void deleteNotification(@PathVariable Integer notification_id) {
        notificationService.deleteNotificationById(notification_id);
    }
}