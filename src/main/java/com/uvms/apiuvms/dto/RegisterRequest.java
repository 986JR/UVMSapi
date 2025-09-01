package com.uvms.apiuvms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for vendor registration requests from mobile and web clients
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    private String first_name;

    @NotBlank(message = "Last name is required")
    @Size(max = 255, message = "Last name cannot exceed 255 characters")
    private String last_name;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Please provide a valid phone number")
    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    private String phone_number;

    @NotBlank(message = "Company name is required")
    @Size(max = 50, message = "Company name cannot exceed 50 characters")
    private String company_name;

    @NotBlank(message = "TIN number is required")
    @Size(max = 25, message = "TIN number cannot exceed 25 characters")
    private String tin_number;

    @NotBlank(message = "Business address is required")
    @Size(max = 255, message = "Business address cannot exceed 255 characters")
    private String business_address;

    @NotBlank(message = "Business type is required")
    @Size(max = 255, message = "Business type cannot exceed 255 characters")
    private String business_type;

    // Optional field for profile picture path
    @Size(max = 255, message = "Profile picture path cannot exceed 255 characters")
    private String profile_picture_path;

    // Constructors
    public RegisterRequest() {}

    public RegisterRequest(String email, String password, String first_name, String last_name,
                           String phone_number, String company_name, String tin_number,
                           String business_address, String business_type) {
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.company_name = company_name;
        this.tin_number = tin_number;
        this.business_address = business_address;
        this.business_type = business_type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getTin_number() {
        return tin_number;
    }

    public void setTin_number(String tin_number) {
        this.tin_number = tin_number;
    }

    public String getBusiness_address() {
        return business_address;
    }

    public void setBusiness_address(String business_address) {
        this.business_address = business_address;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public String getProfile_picture_path() {
        return profile_picture_path;
    }

    public void setProfile_picture_path(String profile_picture_path) {
        this.profile_picture_path = profile_picture_path;
    }
}