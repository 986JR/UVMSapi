package com.uvms.apiuvms.repository;

import com.uvms.apiuvms.entity.Licenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseRepository extends JpaRepository<Licenses, Integer> {
    // Custom queries can be added here
}