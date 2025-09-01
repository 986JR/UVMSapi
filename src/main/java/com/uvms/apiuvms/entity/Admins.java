package com.uvms.apiuvms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name="Admins")
@Data
public class Admins {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int admin_id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password_hash;

   @Column(nullable = false)
   private String name;

   public enum Role {
       COLLEGE_ADMIN,
       SUPER_ADMIN
   }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;



   // @Column(nullable = false)
   // private String role;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime last_login;

    @Column(nullable = false)
    private boolean is_active=true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id", nullable = true, referencedColumnName = "college_id")
    @JsonIgnore
    private Colleges colleges;

    //Constructors


    public Admins() {
    }

    public Admins(int admin_id, String email
            , String password_hash
            , String name, Role role
            , LocalDateTime created_at
            , LocalDateTime last_login
            , boolean is_active
            , Colleges colleges) {
        this.admin_id = admin_id;
        this.email = email;
        this.password_hash = password_hash;
        this.name = name;
        this.role = role;
        this.created_at = created_at;
        this.last_login = last_login;
        this.is_active = is_active;
        this.colleges = colleges;
    }

    //getters and Setters


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getLast_login() {
        return last_login;
    }

    public void setLast_login(LocalDateTime last_login) {
        this.last_login = last_login;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public Colleges getColleges() {
        return colleges;
    }

    public void setColleges(Colleges colleges) {
        this.colleges = colleges;
    }
}
