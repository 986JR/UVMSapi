// Notifications.java in entity folder
package com.uvms.apiuvms.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
public class Notifications {
    public enum RelatedEntity {
        APPLICATION, LICENSE, RENEWAL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notification_id;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendors vendor;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admins admin;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "related_entity", nullable = false)
    private RelatedEntity relatedEntity;

    @Column(name = "related_entity_id", nullable = false)
    private Integer relatedEntityId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public Notifications() {}

    public Notifications(String title, String message, RelatedEntity relatedEntity, Integer relatedEntityId) {
        this.title = title;
        this.message = message;
        this.relatedEntity = relatedEntity;
        this.relatedEntityId = relatedEntityId;
    }

    //Getters and Setters


    public Integer getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(Integer notification_id) {
        this.notification_id = notification_id;
    }

    public Vendors getVendor() {
        return vendor;
    }

    public void setVendor(Vendors vendor) {
        this.vendor = vendor;
    }

    public Admins getAdmin() {
        return admin;
    }

    public void setAdmin(Admins admin) {
        this.admin = admin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean read) {
        isRead = read;
    }

    public RelatedEntity getRelatedEntity() {
        return relatedEntity;
    }

    public void setRelatedEntity(RelatedEntity relatedEntity) {
        this.relatedEntity = relatedEntity;
    }

    public Integer getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(Integer relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}