package com.uvms.apiuvms.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Colleges")
@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "college_id"
)
public class Colleges {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int college_id;

    @Column(nullable = false, unique = true,length = 255)
    private String college_name;
    // @com.fasterxml.jackson.annotation.JsonIgnore
//Refferential Integrity Contrains
    @OneToMany(mappedBy = "colleges", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
   // @com.fasterxml.jackson.annotation.JsonIgnore
    @JsonIdentityReference(alwaysAsId = true)
    private List<Admins> admins = new ArrayList<>();

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIdentityReference(alwaysAsId = true)
    private List<Tenders> tenders = new ArrayList<>();

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIdentityReference(alwaysAsId = true)
    private List<Policies> policies = new ArrayList<>();
    //Constructors

    public Colleges() {
    }

    public Colleges(int college_id, String college_name) {
        this.college_id = college_id;
        this.college_name = college_name;
    }

    //Getter and Setter

    public String getCollege_name() {
        return college_name;
    }

    public void setCollege_name(String college_name) {
        this.college_name = college_name;
    }

}
