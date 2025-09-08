package com.uvms.apiuvms.repository;

import com.uvms.apiuvms.entity.Admins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AdminsRepository extends JpaRepository<Admins, Integer> {

    // Find admin by email (for login)
    Optional<Admins> findByEmail(String email);

    // Update last login timestamp
    @Transactional
    @Modifying
    @Query("UPDATE Admins a SET a.last_login = :lastLogin WHERE a.email = :email")
    void updateLastLogin(String email, LocalDateTime lastLogin);

    // Optional: Check if email already exists (useful for validations)
    boolean existsByEmail(String email);
}
