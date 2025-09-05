package com.uvms.apiuvms.service;

import com.uvms.apiuvms.dto.DashboardResponse;
import com.uvms.apiuvms.dto.RegisterRequest;
import com.uvms.apiuvms.entity.Vendors;
import com.uvms.apiuvms.repository.VendorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class VendorsService {
    @Autowired
    private VendorsRepository vendorsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Getting All Data
    public List<Vendors> getAllVendors() {
        return vendorsRepository.findAll();
    }

    //Getting a Vendor By vendor_id
    public Optional<Vendors> getVendorById(Integer id) {
        return vendorsRepository.findById(id);
    }

    //Saving or Inserting Data To vendor
    public Vendors saveVendor(Vendors vendors) {
        // Validation for new vendors (if vendor_id is 0, it's a new vendor)


        boolean isNewVendor = vendors.getVendorId() == 0;

        if (isNewVendor) {
            // Check if vendor with email already exists
            if (vendorsRepository.existsByEmail(vendors.getEmail())) {
                throw new RuntimeException("Vendor with email already exists: " + vendors.getEmail());
            }

            // Check if phone number already exists
            if (vendorsRepository.existsByPhoneNumber(vendors.getPhoneNumber())) {
                throw new RuntimeException("Vendor with phone number already exists: " + vendors.getPhoneNumber());
            }

            // Check if company name already exists
            if (vendorsRepository.existsByCompanyName(vendors.getCompanyName())) {
                throw new RuntimeException("Vendor with company name already exists: " + vendors.getCompanyName());
            }

            // Check if TIN number already exists
            if (vendorsRepository.existsByTinNumber(vendors.getTinNumber())) {
                throw new RuntimeException("Vendor with TIN number already exists: " + vendors.getTinNumber());
            }

            // Hash password if it's not already hashed (doesn't start with $2a$ which is bcrypt prefix)
            if (!vendors.getPasswordHash().startsWith("$2a$")) {
                vendors.setPasswordHash(passwordEncoder.encode(vendors.getPasswordHash()));
            }

            // Set default values for new vendors
            //if (vendors.getProfilePicturePath() == null || vendors.getProfilePicturePath().isEmpty()) {
               // vendors.setProfilePicturePath("default_profile.png");
           // }

            if (vendors.getRegistrationDate() == null) {
                vendors.setRegistrationDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }

            if (vendors.getLastLogin() == null) {
                vendors.setLastLogin(LocalDateTime.now());
            }

            // Set active by default for new vendors
            vendors.setActive(true);
        }

        return (Vendors) vendorsRepository.save(vendors);
    }

    //Deleting method for Vendors
    public void deleteVendor(Integer id) {
        vendorsRepository.deleteById(id);
    }

    // Authentication and Registration Methods - Using new field names

    /**
     * Register new vendor using the main saveVendor method
     * Uses new camelCase field names
     * @param registerRequest Registration data from client
     * @return Saved vendor entity
     */
    public Vendors registerVendor(RegisterRequest registerRequest) {
        // Create new vendor entity with new field names
        Vendors vendor = new Vendors();
        vendor.setEmail(registerRequest.getEmail());
        vendor.setPasswordHash(registerRequest.getPassword()); // Will be hashed in saveVendor
        vendor.setFirstName(registerRequest.getFirst_name());
        vendor.setLastName(registerRequest.getLast_name());
        vendor.setPhoneNumber(registerRequest.getPhone_number());
        vendor.setCompanyName(registerRequest.getCompany_name());
        vendor.setTinNumber(registerRequest.getTin_number());
        vendor.setBusinessAddress(registerRequest.getBusiness_address());
        vendor.setBusinessType(registerRequest.getBusiness_type());
        vendor.setProfilePicturePath(registerRequest.getProfile_picture_path());

        // Use the main saveVendor method which handles validation and hashing
        return saveVendor(vendor);
    }

    /**
     * Find vendor by email for authentication
     * @param email vendor's email
     * @return Optional<Vendors>
     */
    public Optional<Vendors> getVendorByEmail(String email) {
        return vendorsRepository.findByEmail(email);
    }

    /**
     * Update vendor's last login time
     * @param email vendor's email
     */
    public void updateLastLogin(String email) {
        Optional<Vendors> vendorOptional = vendorsRepository.findByEmail(email);
        if (vendorOptional.isPresent()) {
            Vendors vendor = vendorOptional.get();
            vendor.setLastLogin(LocalDateTime.now());
            vendorsRepository.save(vendor);
        }
    }

    /**
     * Check if vendor exists by email
     * @param email vendor's email
     * @return true if exists
     */
    public boolean existsByEmail(String email) {
        return vendorsRepository.existsByEmail(email);
    }

    /**
     * Activate/Deactivate vendor account
     * @param vendorId vendor's ID
     * @param isActive new status
     * @return updated vendor
     */
    public Optional<Vendors> updateVendorStatus(Integer vendorId, boolean isActive) {
        Optional<Vendors> vendorOptional = vendorsRepository.findById(vendorId);
        if (vendorOptional.isPresent()) {
            Vendors vendor = vendorOptional.get();
            vendor.setActive(isActive);
            return Optional.of(vendorsRepository.save(vendor));
        }
        return Optional.empty();
    }

    /**
     * Change vendor password
     * @param email vendor's email
     * @param newPassword new password (will be hashed)
     * @return true if changed successfully
     */
    public boolean changePassword(String email, String newPassword) {
        Optional<Vendors> vendorOptional = vendorsRepository.findByEmail(email);
        if (vendorOptional.isPresent()) {
            Vendors vendor = vendorOptional.get();
            vendor.setPasswordHash(passwordEncoder.encode(newPassword));
            vendorsRepository.save(vendor);
            return true;
        }
        return false;
    }

    /**
     * Get vendor statistics for dashboard
     * @return vendor counts
     */
    public VendorStats getVendorStatistics() {
        long totalVendors = vendorsRepository.count();
        long activeVendors = vendorsRepository.findAll().stream()
                .mapToLong(v -> v.isActive() ? 1 : 0)
                .sum();
        long inactiveVendors = totalVendors - activeVendors;

        return new VendorStats(totalVendors, activeVendors, inactiveVendors);
    }

    /**
     * Inner class for vendor statistics
     */
    public static class VendorStats {
        private long totalVendors;
        private long activeVendors;
        private long inactiveVendors;

        public VendorStats(long totalVendors, long activeVendors, long inactiveVendors) {
            this.totalVendors = totalVendors;
            this.activeVendors = activeVendors;
            this.inactiveVendors = inactiveVendors;
        }

        // Getters
        public long getTotalVendors() { return totalVendors; }
        public long getActiveVendors() { return activeVendors; }
        public long getInactiveVendors() { return inactiveVendors; }
    }

    /**
     * Get dashboard data for authenticated vendor
     * This method fetches fresh data from database using the vendor's email from JWT
     * @param email vendor's email extracted from JWT token
     * @return DashboardResponse with vendor dashboard data
     * @throws RuntimeException if vendor not found or inactive
     */
    public DashboardResponse getDashboardData(String email) {
        // Find vendor by email (extracted from JWT)
        Optional<Vendors> vendorOptional = vendorsRepository.findByEmail(email);

        // Check if vendor exists
        if (vendorOptional.isEmpty()) {
            throw new RuntimeException("Vendor not found with email: " + email);
        }

        Vendors vendor = vendorOptional.get();

        // Check if vendor account is active
        if (!vendor.isActive()) {
            throw new RuntimeException("Vendor account is not active");
        }

        // Update last login time (optional - shows vendor was active)
        vendor.setLastLogin(LocalDateTime.now());
        vendorsRepository.save(vendor);

        // Create and return dashboard response
        return new DashboardResponse(vendor);
    }

    /**
     * Alternative method: Get dashboard data using vendor ID from JWT
     * @param vendorId vendor's ID extracted from JWT token
     * @return DashboardResponse with vendor dashboard data
     */
    public DashboardResponse getDashboardDataById(Integer vendorId) {
        Optional<Vendors> vendorOptional = vendorsRepository.findById(vendorId);

        if (vendorOptional.isEmpty()) {
            throw new RuntimeException("Vendor not found with ID: " + vendorId);
        }

        Vendors vendor = vendorOptional.get();

        if (!vendor.isActive()) {
            throw new RuntimeException("Vendor account is not active");
        }

        // Update last login time
        vendor.setLastLogin(LocalDateTime.now());
        vendorsRepository.save(vendor);

        return new DashboardResponse(vendor);
    }


    //
    // VendorsService.java

    public Optional<Vendors> patchVendor(Integer vendorId, Vendors vendorDetails) {
        Optional<Vendors> vendorOptional = vendorsRepository.findById(vendorId);

        if (vendorOptional.isPresent()) {
            Vendors vendor = vendorOptional.get();

            if (vendorDetails.getEmail() != null) {
                vendor.setEmail(vendorDetails.getEmail());
            }
            if (vendorDetails.getPasswordHash() != null) {
                // Hash password if it's not already hashed
                String password = vendorDetails.getPasswordHash();
                if (!password.startsWith("$2a$")) {
                    vendor.setPasswordHash(passwordEncoder.encode(password));
                } else {
                    vendor.setPasswordHash(password);
                }
            }
            if (vendorDetails.getFirstName() != null) {
                vendor.setFirstName(vendorDetails.getFirstName());
            }
            if (vendorDetails.getLastName() != null) {
                vendor.setLastName(vendorDetails.getLastName());
            }
            if (vendorDetails.getProfilePicturePath() != null) {
                vendor.setProfilePicturePath(vendorDetails.getProfilePicturePath());
            }
            if (vendorDetails.getPhoneNumber() != null) {
                vendor.setPhoneNumber(vendorDetails.getPhoneNumber());
            }
            if (vendorDetails.getCompanyName() != null) {
                vendor.setCompanyName(vendorDetails.getCompanyName());
            }
            if (vendorDetails.getTinNumber() != null) {
                vendor.setTinNumber(vendorDetails.getTinNumber());
            }
            if (vendorDetails.getBusinessAddress() != null) {
                vendor.setBusinessAddress(vendorDetails.getBusinessAddress());
            }
            if (vendorDetails.getBusinessType() != null) {
                vendor.setBusinessType(vendorDetails.getBusinessType());
            }
            if (vendorDetails.getRegistrationDate() != null) {
                vendor.setRegistrationDate(vendorDetails.getRegistrationDate());
            }
            if (vendorDetails.getLastLogin() != null) {
                vendor.setLastLogin(vendorDetails.getLastLogin());
            }
            // boolean should be patched carefully (default is false, so check explicitly)
            vendor.setActive(vendorDetails.isActive());

            Vendors updatedVendor = vendorsRepository.save(vendor);
            return Optional.of(updatedVendor);
        }

        return Optional.empty();
    }

}