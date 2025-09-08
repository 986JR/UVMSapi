package com.uvms.apiuvms.dto;

import java.time.LocalDateTime;

/**
 * DTO class for admin dashboard response data
 * Contains admin information and general dashboard stats
 */
public class AdminDashboardResponse {

    // Basic admin information
    private Integer adminId;
    private String name;
    private String email;
    private LocalDateTime lastLogin;
    private boolean isActive;

    // Dashboard-specific stats (examples)
    private Integer totalVendors;
    private Integer activeVendors;
    private String welcomeMessage;
    private String accountStatus;

    // Default constructor
    public AdminDashboardResponse() {}

    // Constructor from Admins entity
    public AdminDashboardResponse(com.uvms.apiuvms.entity.Admins admin,
                                  Integer totalVendors, Integer activeVendors) {
        this.adminId = admin.getAdmin_id();
        this.name = admin.getName();
        this.email = admin.getEmail();
        this.lastLogin = admin.getLast_login();
        this.isActive = admin.isIs_active();
        this.totalVendors = totalVendors;
        this.activeVendors = activeVendors;
        this.welcomeMessage = "Welcome back, " + this.name + "!";
        this.accountStatus = this.isActive ? "Active" : "Inactive";
    }

    // Getters and setters
    public Integer getAdminId() { return adminId; }
    public void setAdminId(Integer adminId) { this.adminId = adminId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Integer getTotalVendors() { return totalVendors; }
    public void setTotalVendors(Integer totalVendors) { this.totalVendors = totalVendors; }

    public Integer getActiveVendors() { return activeVendors; }
    public void setActiveVendors(Integer activeVendors) { this.activeVendors = activeVendors; }

    public String getWelcomeMessage() { return welcomeMessage; }
    public void setWelcomeMessage(String welcomeMessage) { this.welcomeMessage = welcomeMessage; }

    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }
}
