// Policies.java in entity folder
package com.uvms.apiuvms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "policies")
@Data
public class Policies {
    public enum Scope {
        UNIVERSITY, COLLEGE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer policy_id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Scope scope;

    @ManyToOne
    @JoinColumn(name = "college_id")
    @JsonIgnore
    private Colleges college;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admins admin;

    @CreationTimestamp
    @Column(name = "date_posted", updatable = false)
    private LocalDateTime datePosted;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Constructors
    public Policies() {}

    public Policies(String title, String content, Scope scope, Admins admin) {
        this.title = title;
        this.content = content;
        this.scope = scope;
        this.admin = admin;
    }

    public Policies(String title, String content, Scope scope, Colleges college, Admins admin) {
        this.title = title;
        this.content = content;
        this.scope = scope;
        this.college = college;
        this.admin = admin;
    }
}