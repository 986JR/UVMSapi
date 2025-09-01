// TendersRepository.java in repository folder
package com.uvms.apiuvms.repository;

import com.uvms.apiuvms.entity.Tenders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TendersRepository extends JpaRepository<Tenders, Integer> {
    // Custom queries can be added here
}