package com.uvms.apiuvms.service;

import com.uvms.apiuvms.entity.Vendors;
import com.uvms.apiuvms.repository.VendorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Custom UserDetailsService implementation for vendor authentication
 * Uses database field naming consistency throughout
 * Supports both Android and Web applications
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private VendorsRepository vendorsRepository;

    /**
     * Load vendor by email for Spring Security authentication
     * @param email vendor's email address (used as username)
     * @return UserDetails object for Spring Security
     * @throws UsernameNotFoundException if vendor not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Find vendor by email using repository method
        Vendors vendor = vendorsRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Vendor not found with email: " + email
                ));

        // Check if vendor account is active
        if (!vendor.isActive()) {
            throw new UsernameNotFoundException(
                    "Vendor account is inactive: " + email
            );
        }

        // Create authorities/roles for the vendor
        Collection<GrantedAuthority> authorities = getVendorAuthorities(vendor);

        // Return Spring Security UserDetails object
        return User.builder()
                .username(vendor.getEmail()) // Email as username
                .password(vendor.getPasswordHash()) // Password hash from database
                .authorities(authorities) // Vendor roles/permissions
                .accountExpired(false) // Account not expired
                .accountLocked(!vendor.isActive()) // Lock if not active
                .credentialsExpired(false) // Credentials not expired
                .disabled(!vendor.isActive()) // Disable if not active
                .build();
    }

    /**
     * Get vendor authorities/roles based on vendor status and business type
     * Maintains database field naming consistency
     * @param vendor Vendor entity
     * @return Collection of authorities
     */
    private Collection<GrantedAuthority> getVendorAuthorities(Vendors vendor) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Base role for all vendors
        authorities.add(new SimpleGrantedAuthority("ROLE_VENDOR"));

        // Additional permissions based on vendor status
        if (vendor.isActive()) {
            authorities.add(new SimpleGrantedAuthority("VENDOR_ACTIVE"));
        }

        // You can add more business logic here based on business_type, etc.
        if (vendor.getBusinessType() != null) {
            String businessTypeRole = "BUSINESS_" + vendor.getBusinessType().toUpperCase().replace(" ", "_");
            authorities.add(new SimpleGrantedAuthority(businessTypeRole));
        }

        return authorities;
    }

    /**
     * Helper method to get Vendor entity by email
     * Useful for services that need full vendor information
     * @param email vendor's email
     * @return Vendor entity
     * @throws UsernameNotFoundException if not found
     */
    public Vendors getVendorByEmail(String email) throws UsernameNotFoundException {
        return vendorsRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Vendor not found with email: " + email
                ));
    }

    /**
     * Check if vendor exists and is active
     * @param email vendor's email
     * @return true if vendor exists and is active
     */
    public boolean isVendorActiveByEmail(String email) {
        return vendorsRepository.findByEmail(email)
                .map(Vendors::isActive)
                .orElse(false);
    }

    /**
     * Get vendor ID by email
     * Useful for JWT token generation
     * @param email vendor's email
     * @return vendor_id
     * @throws UsernameNotFoundException if not found
     */
    public Integer getVendorIdByEmail(String email) throws UsernameNotFoundException {
        return vendorsRepository.findByEmail(email)
                .map(Vendors::getVendorId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Vendor not found with email: " + email
                ));
    }
}