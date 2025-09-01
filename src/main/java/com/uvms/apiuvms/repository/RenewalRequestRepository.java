package com.uvms.apiuvms.repository;

import com.uvms.apiuvms.entity.RenewalRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RenewalRequestRepository extends JpaRepository<RenewalRequests, Integer> {
    // Custom queries can be added here
}