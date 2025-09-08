package com.uvms.apiuvms.controller;

import com.uvms.apiuvms.entity.Applications;
import com.uvms.apiuvms.service.ApplicationService;
import com.uvms.apiuvms.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public List<Applications> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/{application_id}")
    public ResponseEntity<Applications> getApplicationById(@PathVariable Integer application_id) {
        Optional<Applications> application = applicationService.getApplicationById(application_id);
        return application.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Simplified POST: no signedContract required anymore
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createApplication(
            @RequestParam("plot") Integer plotId,
            @RequestParam("tender") Integer tenderId,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            System.out.println("=== Incoming Application Request ===");
            System.out.println("Auth Header: " + authHeader);
            System.out.println("Plot ID: " + plotId);
            System.out.println("Tender ID: " + tenderId);

            // Extract token
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

            // Save application
            Applications app = applicationService.createApplication(vendorId, plotId, tenderId);
            System.out.println("Application created with ID: " + app.getApplication_id());

            return ResponseEntity.ok(app);

        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error occurred");
        }
    }

    // ✅ Keep update with optional contract/status/feedback
    @PutMapping(value = "/{application_id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Applications> updateApplication(
            @PathVariable Integer application_id,
            @RequestParam(value = "status", required = false) Applications.Status status,
            @RequestParam(value = "signedContract", required = false) org.springframework.web.multipart.MultipartFile signedContract,
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
