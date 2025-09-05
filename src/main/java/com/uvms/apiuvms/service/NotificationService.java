package com.uvms.apiuvms.service;

import com.uvms.apiuvms.entity.Notifications;
import com.uvms.apiuvms.dto.NotificationsDTO;
import com.uvms.apiuvms.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<NotificationsDTO> getNotificationsByVendorEmail(String vendorEmail) {
        List<Notifications> notifications = notificationRepository.findByVendorEmail(vendorEmail);

        return notifications.stream().map(n -> new NotificationsDTO(
                n.getNotification_id(),
                n.getVendor() != null ? n.getVendor().getVendorId() : null,
                n.getAdmin() != null ? n.getAdmin().getAdmin_id() : null,
                n.getTitle(),
                n.getMessage(),
                n.getIsRead(),
                n.getRelatedEntity().name(),
                n.getRelatedEntityId(),
                n.getCreatedAt()
        )).collect(Collectors.toList());
    }
}