package com.uvms.apiuvms.service;

import com.uvms.apiuvms.entity.Applications;
import com.uvms.apiuvms.repository.ApplicationRepository;
import com.uvms.apiuvms.repository.PlotRepository;
import com.uvms.apiuvms.repository.VendorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private VendorsRepository vendorsRepository;

    @Autowired
    private PlotRepository plotRepository;

    // Get all applications
    public List<Applications> getAllApplications() {
        return applicationRepository.findAll();
    }

    // Get application by ID
    public Optional<Applications> getApplicationById(Integer id) {
        return applicationRepository.findById(id);
    }

    // Save a new application
    public Applications saveApplication(Applications application) {
        return applicationRepository.save(application);
    }

    // Delete application by ID
    public void deleteApplicationById(Integer id) {
        applicationRepository.deleteById(id);
    }

    // Create application linked with vendor & plot
    public Applications createApplication(Integer vendorId, Integer plotId) {
        Applications app = new Applications();
        app.setVendor(vendorsRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found")));
        app.setPlot(plotRepository.findById(plotId)
                .orElseThrow(() -> new RuntimeException("Plot not found")));
        app.setStatus(Applications.Status.PENDING);
        return applicationRepository.save(app);
    }

    // Update application (status + feedback)
    public Applications updateApplication(Integer applicationId,
                                          Applications.Status status,
                                          String feedback) {
        Applications app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (status != null) app.setStatus(status);
        if (feedback != null) app.setFeedback(feedback);

        return applicationRepository.save(app);
    }
}
