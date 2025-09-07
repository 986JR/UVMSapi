package com.uvms.apiuvms.controller;

import com.uvms.apiuvms.entity.Applications;
import com.uvms.apiuvms.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.uvms.apiuvms.security.JwtUtil; // Added for JWT handling
import org.springframework.http.HttpStatus; // Added for response status


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;


    @Autowired
    private JwtUtil jwtUtil; // Added for extracting vendor ID from JWT


    @GetMapping
    public List<Applications> getAllApplications() {
        return applicationService.getAllApplications();
    }

//    @GetMapping("/status/{status}")
//    public List<Applications> getApplicationsByStatus(@PathVariable Applications.Status status) {
//        return applicationService.getApplicationsByStatus(status);
//    }

    @GetMapping("/{application_id}")
    public ResponseEntity<Applications> getApplicationById(@PathVariable Integer application_id) {
        Optional<Applications> application = applicationService.getApplicationById(application_id);
        return application.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createApplication(
            @RequestParam("plot") Integer plotId,
            @RequestParam("signedContract") MultipartFile signedContract,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            // Extract token from "Bearer <token>"
            String token = jwtUtil.extractTokenFromHeader(authHeader);
            if (token == null || !jwtUtil.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid or missing JWT token");
            }

            // Extract vendor ID from token
            Integer vendorId = jwtUtil.extractVendor_id(token);
            if (vendorId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Vendor ID not found in token");
            }

            // Call service with vendorId instead of raw token
            Applications app = applicationService.createApplication(vendorId, plotId, signedContract);
            return ResponseEntity.ok(app);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error occurred");
        }
    }


    // ✅ Modified PUT to allow file update + status/feedback
    @PutMapping(value = "/{application_id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Applications> updateApplication(
            @PathVariable Integer application_id,
            @RequestParam(value = "status", required = false) Applications.Status status,
            @RequestParam(value = "signedContract", required = false) MultipartFile signedContract,
            @RequestParam(value = "feedback", required = false) String feedback
    ) {
        Applications updatedApp = applicationService.updateApplication(application_id, status, feedback, signedContract);
        return ResponseEntity.ok(updatedApp);
    }

    @DeleteMapping("/{application_id}")
    public void deleteApplication(@PathVariable Integer application_id) {
        applicationService.deleteApplicationById(application_id);
    }
}
