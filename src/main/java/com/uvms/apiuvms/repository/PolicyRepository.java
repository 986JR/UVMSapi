package com.uvms.apiuvms.repository;

import com.uvms.apiuvms.entity.Policies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyRepository extends JpaRepository<Policies, Integer> {
    // Custom queries can be added here
}