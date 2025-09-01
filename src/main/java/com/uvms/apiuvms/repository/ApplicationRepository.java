package com.uvms.apiuvms.repository;

import com.uvms.apiuvms.entity.Applications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Applications, Integer> {
    // Custom queries can be added here
}