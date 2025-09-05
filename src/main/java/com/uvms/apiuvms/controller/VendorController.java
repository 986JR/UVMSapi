package com.uvms.apiuvms.controller;

import com.uvms.apiuvms.entity.Vendors;
import com.uvms.apiuvms.service.VendorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import com.uvms.apiuvms.dto.DashboardResponse;
import com.uvms.apiuvms.security.JwtUtil;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorsService vendorsService;

    @Autowired
    private JwtUtil jwtUtil;

    //Getting all the vendors
    @GetMapping
    public List<Vendors> getAllVendors() {
        return vendorsService.getAllVendors();
    }

    //Getting A Vendor By vendor_id
    @GetMapping("/{vendor_id}")
    public ResponseEntity<Vendors> getVendorById(@PathVariable Integer vendor_id) {
        Optional<Vendors> vendors = vendorsService.getVendorById(vendor_id);

        return vendors
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());

    }

    //Posting to vendor
    @PostMapping
    public void postVendor(@RequestBody Vendors vendors) {
     //   System.out.println("TIN before save: " + vendors.getTinNumber());
        vendorsService.saveVendor(vendors);
    }

    //Putting into Vendors
    @PutMapping("/{vendor_id}")
    public ResponseEntity<Vendors> putVendorById(@PathVariable Integer vendor_id
            , @RequestBody Vendors vendorsDetails) {


        Optional<Vendors> vendorsOptional = vendorsService.getVendorById(vendor_id);

        if(vendorsOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Vendors vendors=vendorsOptional.get();
        vendors.setEmail(vendorsDetails.getEmail());
        vendors.setCompanyName(vendorsDetails.getCompanyName());
        vendors.setBusinessAddress(vendorsDetails.getBusinessAddress());
        vendors.setActive(vendorsDetails.isActive());
        vendors.setPasswordHash(vendorsDetails.getPasswordHash());
        vendors.setProfilePicturePath(vendorsDetails.getProfilePicturePath());
        vendors.setTinNumber(vendorsDetails.getTinNumber());
        vendors.setFirstName(vendorsDetails.getFirstName());
        vendors.setLastName(vendorsDetails.getLastName());
        vendors.setLastLogin(vendorsDetails.getLastLogin());
        vendors.setPhoneNumber(vendorsDetails.getPhoneNumber());

        Vendors updatedVendor =vendorsService.saveVendor(vendors);

        return ResponseEntity.ok(updatedVendor);
    }

    @DeleteMapping("/{vendor_id}")
    public void deleteVendor(@PathVariable Integer vendor_id) {
        vendorsService.deleteVendor(vendor_id);
    }


    /**
     * GET /api/vendors/dashboard
     * Secured endpoint to get dashboard data for authenticated vendor
     * Requires JWT token in Authorization header: "Bearer <token>"
     */
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardData(HttpServletRequest request) {
        try {
            //Extract Authorization header
            String authHeader = request.getHeader("Authorization");

            // Check if Authorization header exists
            if (authHeader == null || authHeader.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing Authorization header");
            }

            // Extract JWT token from "Bearer <token>" format
            String token = jwtUtil.extractTokenFromHeader(authHeader);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid Authorization header format. Use: Bearer <token>");
            }

            //Validate JWT token
            if (!jwtUtil.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid or expired JWT token");
            }

            //Extract vendor email from JWT token
            String vendorEmail = jwtUtil.extractEmail(token);
            if (vendorEmail == null || vendorEmail.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid JWT token: missing email");
            }

            // Get dashboard data using the service
            DashboardResponse dashboardData = vendorsService.getDashboardData(vendorEmail);

            //Return successful response
            return ResponseEntity.ok(dashboardData);

        } catch (RuntimeException e) {
            // Handle service exceptions (vendor not found, inactive account, etc.)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            // Handle any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error occurred");
        }
    }

    /**
     * Alternative endpoint using vendor ID from JWT (optional)
     * GET /api/vendors/dashboard-by-id
     */
    @GetMapping("/dashboard-by-id")
    public ResponseEntity<?> getDashboardDataById(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || authHeader.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing Authorization header");
            }

            String token = jwtUtil.extractTokenFromHeader(authHeader);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid Authorization header format");
            }

            if (!jwtUtil.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid or expired JWT token");
            }

            // Extract vendor ID instead of email
            Integer vendorId = jwtUtil.extractVendor_id(token);
            if (vendorId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid JWT token: missing vendor ID");
            }

            DashboardResponse dashboardData = vendorsService.getDashboardDataById(vendorId);

            return ResponseEntity.ok(dashboardData);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error occurred");
        }
    }


    //
    // VendorController.java

    @PatchMapping("/{vendor_id}")
    public ResponseEntity<Vendors> patchVendor(
            @PathVariable Integer vendor_id,
            @RequestBody Vendors vendorDetails) {

        Optional<Vendors> updatedVendor = vendorsService.patchVendor(vendor_id, vendorDetails);

        return updatedVendor
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}