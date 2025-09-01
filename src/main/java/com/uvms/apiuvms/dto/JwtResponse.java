package com.uvms.apiuvms.dto;

import lombok.Data;

/**
 * DTO for JWT authentication responses sent to mobile and web clients
 */
@Data
public class JwtResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn; // Token expiration time in seconds

    // Vendor information
    private Integer vendor_id;
    private String email;
    private String first_name;
    private String last_name;
    private String company_name;
    private String tin_number;
    private boolean is_active;

    // Success/Error messaging
    private String message;
    private boolean success;

    // Constructors
    public JwtResponse() {}

    public JwtResponse(String accessToken, Long expiresIn, Integer vendor_id,
                       String email, String first_name, String last_name,String tin_number,
                       String company_name, boolean is_active) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.vendor_id = vendor_id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.company_name = company_name;
        this.tin_number= tin_number;
        this.is_active = is_active;
        this.success = true;
        this.message = "Authentication successful";
    }

    // Constructor for error responses
    public JwtResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    // Static factory methods for common responses
    public static JwtResponse success(String accessToken, Long expiresIn, Integer vendor_id,
                                      String email, String first_name, String last_name,String tin_number,
                                      String company_name, boolean is_active) {
        return new JwtResponse(accessToken, expiresIn, vendor_id, email, first_name,tin_number,
                last_name, company_name, is_active);
    }

    public static JwtResponse error(String message) {
        return new JwtResponse(message, false);
    }

    //Getters and Setters

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

    public Integer getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(Integer vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
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

    public String getTin_number() {
        return tin_number;
    }

    public void setTin_number(String tin_number) {
        this.tin_number = tin_number;
    }

    // Add this getter for backward compatibility
    public String getToken() {
        return this.accessToken;
    }
}