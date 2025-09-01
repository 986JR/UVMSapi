package com.uvms.apiuvms.controller;

import com.uvms.apiuvms.entity.Applications;
import com.uvms.apiuvms.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping
    public List<Applications> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/status/{status}")
    public List<Applications> getApplicationsByStatus(@PathVariable Applications.Status status) {
        return applicationService.getApplicationsByStatus(status);
    }

    @GetMapping("/{application_id}")
    public ResponseEntity<Applications> getApplicationById(@PathVariable Integer application_id) {
        Optional<Applications> application = applicationService.getApplicationById(application_id);
        return application.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Applications createApplication(@RequestBody Applications application) {
        return applicationService.saveApplication(application);
    }

    @PutMapping("/{application_id}")
    public ResponseEntity<Applications> updateApplication(@PathVariable Integer application_id,
                                                          @RequestBody Applications applicationDetails) {
        Optional<Applications> applicationOptional = applicationService.getApplicationById(application_id);

        if (applicationOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Applications application = applicationOptional.get();
        application.setStatus(applicationDetails.getStatus());
        application.setSubmittedContractPath(applicationDetails.getSubmittedContractPath());
        application.setApprovedContractPath(applicationDetails.getApprovedContractPath());
        application.setFeedback(applicationDetails.getFeedback());

        Applications updatedApplication = applicationService.saveApplication(application);
        return ResponseEntity.ok(updatedApplication);
    }

    @DeleteMapping("/{application_id}")
    public void deleteApplication(@PathVariable Integer application_id) {
        applicationService.deleteApplicationById(application_id);
    }
}