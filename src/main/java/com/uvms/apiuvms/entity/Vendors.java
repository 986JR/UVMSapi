package com.uvms.apiuvms.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "vendors")
@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "vendorId"
)
@JsonIdentityReference(alwaysAsId = true)
public class Vendors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendor_id")
    private int vendorId;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, unique = true, length = 500)
    private String passwordHash;

    @Column(name = "first_name", nullable = false, unique = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, unique = false, length = 255)
    private String lastName;

    @Column(name = "profile_picture_path", nullable = true, unique = false, length = 255)
    private String profilePicturePath;//It should be unique = true

    @Column(name = "phone_number", nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(name = "company_name", nullable = false, unique = true, length = 50)
    private String companyName;

    @Column(name = "tin_number", nullable = false, unique = true, length = 25)
    private String tinNumber;

    @Column(name = "business_address", nullable = false, unique = false, length = 255)
    private String businessAddress;

    @Column(name = "business_type", nullable = false, unique = false, length = 255)
    private String businessType;

    @Column(name = "registration_date", nullable = false, unique = false, length = 255)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String registrationDate;

    @Column(name = "last_login", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLogin;

    @Column(name = "is_active")
    private boolean isActive = false;

    // Default constructor
    public Vendors() {
    }

    // All-args constructor
    public Vendors(int vendorId, String email, String passwordHash,
                   String firstName, String lastName, String profilePicturePath,
                   String phoneNumber, String companyName, String tinNumber,
                   String businessAddress, String businessType, String registrationDate,
                   LocalDateTime lastLogin, boolean isActive) {
        this.vendorId = vendorId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicturePath = profilePicturePath;
        this.phoneNumber = phoneNumber;
        this.companyName = companyName;
        this.tinNumber = tinNumber;
        this.businessAddress = businessAddress;
        this.businessType = businessType;
        this.registrationDate = registrationDate;
        this.lastLogin = lastLogin;
        this.isActive = isActive;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }
}