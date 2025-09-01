// Applications.java in entity folder
package com.uvms.apiuvms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
    private Vendors vendor;

    //@ManyToOne
   // @JoinColumn(name = "plot_id", nullable = false)
    //@JsonBackReference
    //private Plots plot;

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
    private Admins reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    private Licenses license;

    // Constructors
    public Applications() {}

    public Applications(Vendors vendor, /*Plots plot,*/ String submittedContractPath) {
        this.vendor = vendor;
       // this.plot = plot;
        this.submittedContractPath = submittedContractPath;
    }
}