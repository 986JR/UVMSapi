package com.uvms.apiuvms.repository;

import com.uvms.apiuvms.entity.Vendors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorsRepository extends JpaRepository<Vendors,Integer> {

    Optional<Vendors> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByCompanyName(String companyName);

    boolean existsByTinNumber(String tinNumber);
}