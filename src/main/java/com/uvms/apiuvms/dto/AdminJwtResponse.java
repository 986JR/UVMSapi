package com.uvms.apiuvms.dto;

import lombok.Data;

/**
 * DTO for JWT authentication responses sent to admins
 */
@Data
public class AdminJwtResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn; // Token expiration time in seconds

    // Admin information
    private Integer admin_id;
    private String email;
    private String name;
    private String role;
    private boolean is_active;

    // Success/Error messaging
    private String message;
    private boolean success;

    // Constructors
    public AdminJwtResponse() {}

    public AdminJwtResponse(String accessToken, Long expiresIn, Integer admin_id,
                            String email, String name, String role, boolean is_active) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.admin_id = admin_id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.is_active = is_active;
        this.success = true;
        this.message = "Authentication successful";
    }

    // Constructor for error responses
    public AdminJwtResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    // Static factory methods for common responses
    public static AdminJwtResponse success(String accessToken, Long expiresIn, Integer admin_id,
                                           String email, String name, String role, boolean is_active) {
        return new AdminJwtResponse(accessToken, expiresIn, admin_id, email, name, role, is_active);
    }

    public static AdminJwtResponse error(String message) {
        return new AdminJwtResponse(message, false);
    }

    // Getters and Setters

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Integer getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(Integer admin_id) {
        this.admin_id = admin_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    // Add this getter for backward compatibility
    public String getToken() {
        return this.accessToken;
    }
}
