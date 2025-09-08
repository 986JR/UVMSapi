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

    // ✅ POST JSON payload
    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> createApplication(
            @RequestBody CreateApplicationRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            // Extract token
            String token = jwtUtil.extractTokenFromHeader(authHeader);
            if (token == null || !jwtUtil.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid or missing JWT token");
            }

            // Extract vendor ID from token
            Integer vendorId = jwtUtil.extractVendorId(token);
            if (vendorId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Vendor ID not found in token");
            }

            // Save application
            Applications app = applicationService.createApplication(vendorId, request.getPlot());
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

    @PutMapping("/{application_id}")
    public ResponseEntity<Applications> updateApplication(
            @PathVariable Integer application_id,
            @RequestBody UpdateApplicationRequest request
    ) {
        Applications updatedApp = applicationService.updateApplication(application_id, request.getStatus(), request.getFeedback());
        return ResponseEntity.ok(updatedApp);
    }

    @DeleteMapping("/{application_id}")
    public void deleteApplication(@PathVariable Integer application_id) {
        applicationService.deleteApplicationById(application_id);
    }

    // ✅ Request DTOs
    public static class CreateApplicationRequest {
        private Integer plot;

        public Integer getPlot() { return plot; }
        public void setPlot(Integer plot) { this.plot = plot; }
    }

    public static class UpdateApplicationRequest {
        private Applications.Status status;
        private String feedback;

        public Applications.Status getStatus() { return status; }
        public void setStatus(Applications.Status status) { this.status = status; }

        public String getFeedback() { return feedback; }
        public void setFeedback(String feedback) { this.feedback = feedback; }
    }
}
