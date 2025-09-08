package com.uvms.apiuvms.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "application_id"
)
@Entity
@Table(name = "applications")
@Data
public class Applications {
    public enum Status {
        PENDING, APPROVED, DENIED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer application_id;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Vendors vendor;

    @ManyToOne
    @JoinColumn(name = "plot_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Plots plot;

    // 🔹 New relation: Many Applications belong to One Tender
    @ManyToOne
    @JoinColumn(name = "tender_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Tenders tender;

    @CreationTimestamp
    @Column(name = "application_date", updatable = false)
    private LocalDateTime applicationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "submitted_contract_path", length = 255)
    private String submittedContractPath;

    @Column(name = "approved_contract_path", length = 255)
    private String approvedContractPath;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    @JsonIdentityReference(alwaysAsId = true)
    private Admins reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    @JsonIdentityReference(alwaysAsId = true)
    private Licenses license;

    // Constructors
    public Applications() {}

    public Applications(Vendors vendor, Plots plot, Tenders tender, String submittedContractPath) {
        this.vendor = vendor;
        this.plot = plot;
        this.tender = tender;
        this.submittedContractPath = submittedContractPath;
    }
}
