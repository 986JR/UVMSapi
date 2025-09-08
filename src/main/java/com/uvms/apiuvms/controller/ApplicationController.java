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

    // ✅ GET all applications
    @GetMapping
    public List<Applications> getAllApplications() {
        return applicationService.getAllApplications();
    }

    // ✅ GET application by ID
    @GetMapping("/{application_id}")
    public ResponseEntity<Applications> getApplicationById(@PathVariable Integer application_id) {
        Optional<Applications> application = applicationService.getApplicationById(application_id);
        return application.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ POST to create new application (JSON request)
    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> createApplication(
            @RequestBody CreateApplicationRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            // Extract token and vendorId
            String token = jwtUtil.extractTokenFromHeader(authHeader);
            if (token == null || !jwtUtil.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid or missing JWT token");
            }
            Integer vendorId = jwtUtil.extractVendor_id(token);
            if (vendorId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Vendor ID not found in token");
            }

            // Save application
            Applications app = applicationService.createApplication(vendorId, request.getPlotId());
            return ResponseEntity.ok(app);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error occurred");
        }
    }

    // ✅ PUT to update application (JSON request)
    @PutMapping("/{application_id}")
    public ResponseEntity<Applications> updateApplication(
            @PathVariable Integer application_id,
            @RequestBody UpdateApplicationRequest request
    ) {
        Applications updatedApp = applicationService.updateApplication(
                application_id,
                request.getStatus(),
                request.getFeedback()
        );
        return ResponseEntity.ok(updatedApp);
    }

    // ✅ DELETE application
    @DeleteMapping("/{application_id}")
    public void deleteApplication(@PathVariable Integer application_id) {
        applicationService.deleteApplicationById(application_id);
    }

    // ---------------- DTOs ----------------
    public static class CreateApplicationRequest {
        private Integer plotId;

        public Integer getPlotId() { return plotId; }
        public void setPlotId(Integer plotId) { this.plotId = plotId; }
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
