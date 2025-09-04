// Licenses.java in entity folder
package com.uvms.apiuvms.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "licenses")
@Data

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "license_id"
)

public class Licenses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer license_id;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private Applications application;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendors vendor;

    @Column(name = "license_number", length = 100, unique = true)
    private String licenseNumber;

    @Column(name = "issue_date")
    private LocalDateTime issueDate;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "license_file_path", length = 255)
    private String licenseFilePath;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "license", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RenewalRequests> renewalRequests = new ArrayList<>();

    // Constructors
    public Licenses() {}

    public Licenses(Applications application, Vendors vendor, String licenseNumber,
                    LocalDateTime issueDate, LocalDateTime expiryDate, String licenseFilePath) {
        this.application = application;
        this.vendor = vendor;
        this.licenseNumber = licenseNumber;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.licenseFilePath = licenseFilePath;
    }
}