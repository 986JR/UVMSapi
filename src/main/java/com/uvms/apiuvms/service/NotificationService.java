package com.uvms.apiuvms.service;

import com.uvms.apiuvms.entity.Notifications;
import com.uvms.apiuvms.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notifications> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Optional<Notifications> getNotificationById(Integer id) {
        return notificationRepository.findById(id);
    }

    public Notifications saveNotification(Notifications notification) {
        return notificationRepository.save(notification);
    }

    public void deleteNotificationById(Integer id) {
        notificationRepository.deleteById(id);
    }

    // Additional methods for business logic
    public List<Notifications> getUnreadNotifications() {
        return notificationRepository.findAll().stream()
                .filter(notification -> !notification.getIsRead())
                .toList();
    }

    public void markAsRead(Integer notificationId) {
        Optional<Notifications> notificationOptional = notificationRepository.findById(notificationId);
        if (notificationOptional.isPresent()) {
            Notifications notification = notificationOptional.get();
            notification.setIsRead(true);
            notificationRepository.save(notification);
        }
    }
}