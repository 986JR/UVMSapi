package com.uvms.apiuvms.controller;

import com.uvms.apiuvms.entity.Licenses;
import com.uvms.apiuvms.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/licenses")
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @GetMapping
    public List<Licenses> getAllLicenses() {
        return licenseService.getAllLicenses();
    }

    @GetMapping("/active")
    public List<Licenses> getActiveLicenses() {
        return licenseService.getActiveLicenses();
    }

    @GetMapping("/{license_id}")
    public ResponseEntity<Licenses> getLicenseById(@PathVariable Integer license_id) {
        Optional<Licenses> license = licenseService.getLicenseById(license_id);
        return license.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Licenses createLicense(@RequestBody Licenses license) {
        return licenseService.saveLicense(license);
    }

    @PutMapping("/{license_id}")
    public ResponseEntity<Licenses> updateLicense(@PathVariable Integer license_id,
                                                  @RequestBody Licenses licenseDetails) {
        Optional<Licenses> licenseOptional = licenseService.getLicenseById(license_id);

        if (licenseOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Licenses license = licenseOptional.get();
        license.setLicenseNumber(licenseDetails.getLicenseNumber());
        license.setIssueDate(licenseDetails.getIssueDate());
        license.setExpiryDate(licenseDetails.getExpiryDate());
        license.setLicenseFilePath(licenseDetails.getLicenseFilePath());
        license.setIsActive(licenseDetails.getIsActive());

        Licenses updatedLicense = licenseService.saveLicense(license);
        return ResponseEntity.ok(updatedLicense);
    }

    @DeleteMapping("/{license_id}")
    public void deleteLicense(@PathVariable Integer license_id) {
        licenseService.deleteLicenseById(license_id);
    }
}