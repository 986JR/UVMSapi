// Tenders.java in entity folder
package com.uvms.apiuvms.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tenders")
@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "tender_id"
)
public class Tenders {
    public enum Status {
        ACTIVE, EXPIRED, PENDING, REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tender_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Colleges college;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "deadline_date")
    private LocalDateTime deadlineDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "contract_template_path", length = 255)
    private String contractTemplatePath;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private Admins createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "tender", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Plots> plots = new ArrayList<>();

    // Constructors
    public Tenders() {}

    public Tenders(Colleges college, String title, String description,
                   LocalDateTime deadlineDate, Status status,
                   String contractTemplatePath, Admins createdBy) {
        this.college = college;
        this.title = title;
        this.description = description;
        this.deadlineDate = deadlineDate;
        this.status = status;
        this.contractTemplatePath = contractTemplatePath;
        this.createdBy = createdBy;
    }
}