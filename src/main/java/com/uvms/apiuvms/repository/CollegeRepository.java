package com.uvms.apiuvms.repository;

import com.uvms.apiuvms.entity.Colleges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollegeRepository extends JpaRepository<Colleges, Integer> {

}
