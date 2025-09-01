package com.uvms.apiuvms.controller;

import com.uvms.apiuvms.dto.JwtResponse;
import com.uvms.apiuvms.dto.LoginRequest;
import com.uvms.apiuvms.dto.RegisterRequest;
import com.uvms.apiuvms.entity.Vendors;
import com.uvms.apiuvms.security.JwtUtil;
import com.uvms.apiuvms.service.UserDetailsServiceImpl;
import com.uvms.apiuvms.service.VendorsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller for vendor login, registration, and logout
 * Supports both Android and Web applications
 * Uses new camelCase field names
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private VendorsService vendorsService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/register")
    public ResponseEntity<?> registerVendor(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Register new vendor using the service
            Vendors newVendor = vendorsService.registerVendor(registerRequest);

            // Load user details for JWT generation
            UserDetails userDetails = userDetailsService.loadUserByUsername(newVendor.getEmail());

            // Generate JWT token with vendor information
            String jwt = jwtUtil.generateToken(userDetails, newVendor);

            // Create success response with updated field names
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setAccessToken(jwt);
            jwtResponse.setTokenType("Bearer");
            jwtResponse.setExpiresIn(jwtUtil.getExpirationInSeconds());
            jwtResponse.setVendor_id(newVendor.getVendorId());
            jwtResponse.setEmail(newVendor.getEmail());
            jwtResponse.setFirst_name(newVendor.getFirstName());
            jwtResponse.setLast_name(newVendor.getLastName());
            jwtResponse.setCompany_name(newVendor.getCompanyName());
            jwtResponse.setIs_active(newVendor.isActive());
            jwtResponse.setSuccess(true);
            jwtResponse.setMessage("Registration successful");

            return ResponseEntity.ok(jwtResponse);

        } catch (RuntimeException e) {
            // Handle validation errors (duplicate email, phone, etc.)
            return ResponseEntity.badRequest()
                    .body(JwtResponse.error("Registration failed: " + e.getMessage()));
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.internalServerError()
                    .body(JwtResponse.error("Registration failed due to server error"));
        }
    }

    /**
     * Vendor Login Endpoint
     * POST /api/auth/login
     *
     * @param loginRequest Login credentials (email & password)
     * @return JWT response with vendor information
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginVendor(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate vendor credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Load vendor details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Vendors vendor = userDetailsService.getVendorByEmail(loginRequest.getEmail());

            // Update last login time
            vendorsService.updateLastLogin(vendor.getEmail());

            // Generate JWT token with vendor information
            String jwt = jwtUtil.generateToken(userDetails, vendor);

            // Create success response with updated field names
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setAccessToken(jwt);
            jwtResponse.setTokenType("Bearer");
            jwtResponse.setExpiresIn(jwtUtil.getExpirationInSeconds());
            jwtResponse.setVendor_id(vendor.getVendorId());
            jwtResponse.setEmail(vendor.getEmail());
            jwtResponse.setFirst_name(vendor.getFirstName());
            jwtResponse.setLast_name(vendor.getLastName());
            jwtResponse.setCompany_name(vendor.getCompanyName());
            jwtResponse.setIs_active(vendor.isActive());
            jwtResponse.setSuccess(true);
            jwtResponse.setMessage("Login successful!!");
            System.out.println("TESTING LOGIN HERE "+jwtResponse.getMessage());
            return ResponseEntity.ok(jwtResponse);

        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest()
                    .body(JwtResponse.error("Invalid email or password"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(JwtResponse.error("Login failed: " + e.getMessage()));
        }
    }

    /**
     * Vendor Logout Endpoint
     * POST /api/auth/logout
     *
     * Note: JWT is stateless, so logout is handled client-side by removing the token
     * This endpoint provides a standard response for client apps
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutVendor(HttpServletRequest request) {
        try {
            // Extract vendor info from request attributes (set by JWT filter)
            String email = (String) request.getAttribute("email");

            if (email != null) {
                // Optional: Update last logout time or perform cleanup
                // vendorsService.updateLastLogout(email);
            }

            // Return success response
            JwtResponse response = new JwtResponse();
            response.setSuccess(true);
            response.setMessage("Logout successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.ok(JwtResponse.error("Logout completed"));
        }
    }

    /**
     * Get Current Vendor Profile
     * GET /api/auth/profile
     *
     * @param request HTTP request with JWT token
     * @return Current vendor information
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentVendor(HttpServletRequest request) {
        try {
            // Extract vendor information from request attributes (set by JWT filter)
            Integer vendorId = (Integer) request.getAttribute("vendor_id");
            String email = (String) request.getAttribute("email");

            if (vendorId == null || email == null) {
                return ResponseEntity.badRequest()
                        .body(JwtResponse.error("Authentication required"));
            }

            // Get full vendor details
            Vendors vendor = userDetailsService.getVendorByEmail(email);

            // Create response with updated field names
            JwtResponse response = new JwtResponse();
            response.setVendor_id(vendor.getVendorId());
            response.setEmail(vendor.getEmail());
            response.setFirst_name(vendor.getFirstName());
            response.setLast_name(vendor.getLastName());
            response.setCompany_name(vendor.getCompanyName());
            response.setIs_active(vendor.isActive());
            response.setSuccess(true);
            response.setMessage("Profile retrieved successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(JwtResponse.error("Failed to retrieve profile: " + e.getMessage()));
        }
    }

    /**
     * Refresh JWT Token
     * POST /api/auth/refresh
     *
     * @param request HTTP request with current JWT token
     * @return New JWT token
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            String jwt = jwtUtil.extractTokenFromHeader(authHeader);

            if (jwt == null || !jwtUtil.isTokenValid(jwt)) {
                return ResponseEntity.badRequest()
                        .body(JwtResponse.error("Invalid token"));
            }

            // Extract email from current token
            String email = jwtUtil.extractEmail(jwt);

            // Load vendor and generate new token
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            Vendors vendor = userDetailsService.getVendorByEmail(email);
            String newJwt = jwtUtil.generateToken(userDetails, vendor);

            // Create response
            JwtResponse response = new JwtResponse();
            response.setAccessToken(newJwt);
            response.setTokenType("Bearer");
            response.setExpiresIn(jwtUtil.getExpirationInSeconds());
            response.setSuccess(true);
            response.setMessage("Token refreshed successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(JwtResponse.error("Token refresh failed: " + e.getMessage()));
        }
    }
}