package com.uvms.apiuvms.repository;

import com.uvms.apiuvms.entity.Plots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlotRepository extends JpaRepository<Plots, Integer> {
    // Custom queries can be added here
}