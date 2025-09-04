// RenewalRequests.java in entity folder
package com.uvms.apiuvms.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "renewal_requests")
@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "renewal_id"
)
@JsonIdentityReference(alwaysAsId = true)
public class RenewalRequests {
    public enum Status {
        PENDING, APPROVED, DENIED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer renewal_id;

    @ManyToOne
    @JoinColumn(name = "license_id", nullable = false)
    private Licenses license;

    @CreationTimestamp
    @Column(name = "request_date", updatable = false)
    private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    // Constructors
    public RenewalRequests() {}

    public RenewalRequests(Licenses license) {
        this.license = license;
    }
}
