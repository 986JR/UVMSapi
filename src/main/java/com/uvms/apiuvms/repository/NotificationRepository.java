package com.uvms.apiuvms.repository;

import com.uvms.apiuvms.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Integer> {
    List<Notifications> findByVendorEmail(String email);
    // Custom queries can be added here
}