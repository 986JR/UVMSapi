package com.uvms.apiuvms.dto;

import java.time.LocalDateTime;

/**
 * DTO class for dashboard response data
 * Contains vendor information to be displayed on the dashboard
 */
public class DashboardResponse {

    // Basic vendor information
    private int vendorId;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;

    // Company information
    private String companyName;
    private String tinNumber;
    private String businessAddress;
    private String businessType;
    private String phoneNumber;

    // Account information
    private String profilePicturePath;
    private String registrationDate;
    private LocalDateTime lastLogin;
    private boolean isActive;

    // Dashboard-specific data
    private String welcomeMessage;
    private String accountStatus;

    // Default constructor
    public DashboardResponse() {
    }

    // Constructor from Vendors entity
    public DashboardResponse(com.uvms.apiuvms.entity.Vendors vendor) {
        this.vendorId = vendor.getVendorId();
        this.email = vendor.getEmail();
        this.firstName = vendor.getFirstName();
        this.lastName = vendor.getLastName();
        this.fullName = vendor.getFirstName() + " " + vendor.getLastName();
        this.companyName = vendor.getCompanyName();
        this.tinNumber = vendor.getTinNumber();
        this.businessAddress = vendor.getBusinessAddress();
        this.businessType = vendor.getBusinessType();
        this.phoneNumber = vendor.getPhoneNumber();
        this.profilePicturePath = vendor.getProfilePicturePath();
        this.registrationDate = vendor.getRegistrationDate();
        this.lastLogin = vendor.getLastLogin();
        this.isActive = vendor.isActive();

        // Set dashboard-specific data
        this.welcomeMessage = "Welcome back, " + this.firstName + "!";
        this.accountStatus = this.isActive ? "Active" : "Inactive";
    }

    // All getters and setters
    public int getVendorId() { return vendorId; }
    public void setVendorId(int vendorId) { this.vendorId = vendorId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getTinNumber() { return tinNumber; }
    public void setTinNumber(String tinNumber) { this.tinNumber = tinNumber; }

    public String getBusinessAddress() { return businessAddress; }
    public void setBusinessAddress(String businessAddress) { this.businessAddress = businessAddress; }

    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getProfilePicturePath() { return profilePicturePath; }
    public void setProfilePicturePath(String profilePicturePath) { this.profilePicturePath = profilePicturePath; }

    public String getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public String getWelcomeMessage() { return welcomeMessage; }
    public void setWelcomeMessage(String welcomeMessage) { this.welcomeMessage = welcomeMessage; }

    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }
}