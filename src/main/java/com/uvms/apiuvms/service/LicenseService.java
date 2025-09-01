package com.uvms.apiuvms.service;

import com.uvms.apiuvms.entity.Licenses;
import com.uvms.apiuvms.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    public List<Licenses> getAllLicenses() {
        return licenseRepository.findAll();
    }

    public Optional<Licenses> getLicenseById(Integer id) {
        return licenseRepository.findById(id);
    }

    public Licenses saveLicense(Licenses license) {
        return licenseRepository.save(license);
    }

    public void deleteLicenseById(Integer id) {
        licenseRepository.deleteById(id);
    }

    // Additional methods for business logic
    public List<Licenses> getActiveLicenses() {
        return licenseRepository.findAll().stream()
                .filter(Licenses::getIsActive)
                .toList();
    }
}