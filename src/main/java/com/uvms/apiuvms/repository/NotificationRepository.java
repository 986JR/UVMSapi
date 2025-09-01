package com.uvms.apiuvms.repository;

import com.uvms.apiuvms.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Integer> {
    // Custom queries can be added here
}