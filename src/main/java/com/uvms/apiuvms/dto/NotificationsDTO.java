// NotificationsDTO.java
package com.uvms.apiuvms.dto;

import java.time.LocalDateTime;

public class NotificationsDTO {
    private Integer notificationId;
    private Integer vendorId;
    private Integer adminId;
    private String title;
    private String message;
    private Boolean isRead;
    private String relatedEntity;
    private Integer relatedEntityId;
    private LocalDateTime createdAt;

    // Constructors
    public NotificationsDTO() {}

    public NotificationsDTO(Integer notificationId, Integer vendorId, Integer adminId,
                            String title, String message, Boolean isRead,
                            String relatedEntity, Integer relatedEntityId,
                            LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.vendorId = vendorId;
        this.adminId = adminId;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
        this.relatedEntity = relatedEntity;
        this.relatedEntityId = relatedEntityId;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public Integer getNotificationId() { return notificationId; }
    public void setNotificationId(Integer notificationId) { this.notificationId = notificationId; }

    public Integer getVendorId() { return vendorId; }
    public void setVendorId(Integer vendorId) { this.vendorId = vendorId; }

    public Integer getAdminId() { return adminId; }
    public void setAdminId(Integer adminId) { this.adminId = adminId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public String getRelatedEntity() { return relatedEntity; }
    public void setRelatedEntity(String relatedEntity) { this.relatedEntity = relatedEntity; }

    public Integer getRelatedEntityId() { return relatedEntityId; }
    public void setRelatedEntityId(Integer relatedEntityId) { this.relatedEntityId = relatedEntityId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
