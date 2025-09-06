// Plots.java in entity folder
package com.uvms.apiuvms.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "plots")
@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "plot_id"
)
public class Plots {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer plot_id;

    @ManyToOne
    @JoinColumn(name = "tender_id", nullable = false)
    @JsonBackReference("tender-plots")
    private Tenders tender;

    @Column(name = "plot_number", length = 50, nullable = false)
    private String plotNumber;

    @Column(name = "location_description", columnDefinition = "TEXT")
    private String locationDescription;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @OneToMany(mappedBy = "plot", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIdentityReference(alwaysAsId = true)
    private List<Applications> application;

    // Constructors
    public Plots() {}

    public Plots(Tenders tender, String plotNumber, String locationDescription, Boolean isAvailable) {
        this.tender = tender;
        this.plotNumber = plotNumber;
        this.locationDescription = locationDescription;
        this.isAvailable = isAvailable;
    }
}