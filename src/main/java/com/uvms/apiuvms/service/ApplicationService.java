package com.uvms.apiuvms.service;

import com.uvms.apiuvms.entity.Applications;
import com.uvms.apiuvms.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public List<Applications> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Optional<Applications> getApplicationById(Integer id) {
        return applicationRepository.findById(id);
    }

    public Applications saveApplication(Applications application) {
        return applicationRepository.save(application);
    }

    public void deleteApplicationById(Integer id) {
        applicationRepository.deleteById(id);
    }

    // Additional methods for business logic
    public List<Applications> getApplicationsByStatus(Applications.Status status) {
        return applicationRepository.findAll().stream()
                .filter(app -> app.getStatus() == status)
                .toList();
    }
}