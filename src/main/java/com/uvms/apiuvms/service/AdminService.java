package com.uvms.apiuvms.service;

import com.uvms.apiuvms.dto.AdminDashboardResponse;
import com.uvms.apiuvms.entity.Admins;
import com.uvms.apiuvms.repository.AdminsRepository;
import com.uvms.apiuvms.repository.VendorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminsRepository adminsRepository;

    @Autowired
    private VendorsRepository vendorsRepository; // Needed for dashboard stats

    // ------------------- CRUD -------------------

    // Get all admins
    public List<Admins> getAllAdmins() {
        return adminsRepository.findAll();
    }

    // Get admin by ID
    public Optional<Admins> getAdminsById(Integer id) {
        return adminsRepository.findById(id);
    }

    // Add or update an admin
    public Admins addAdmin(Admins admins) {
        return adminsRepository.save(admins);
    }

    // Delete admin by ID
    public void deleteAdminById(Integer id) {
        adminsRepository.deleteById(id);
    }

    // ------------------- CUSTOM METHODS -------------------

    // Find admin by email (used during login)
    public Optional<Admins> getAdminByEmail(String email) {
        return adminsRepository.findByEmail(email);
    }

    // Check if an email already exists (useful for registration validation)
    public boolean emailExists(String email) {
        return adminsRepository.existsByEmail(email);
    }

    // Update last login timestamp
    public void updateLastLogin(String email) {
        adminsRepository.updateLastLogin(email, LocalDateTime.now());
    }

    // ------------------- DASHBOARD -------------------

    /**
     * Get dashboard data for a specific admin
     * @param adminId the ID of the admin
     * @return AdminDashboardResponse DTO
     */
    public AdminDashboardResponse getDashboardDataById(Integer adminId) {
        // Find admin by ID
        Admins admin = adminsRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Example dashboard statistics
        int totalVendors = (int) vendorsRepository.count(); // Total registered vendors
        int activeVendors = (int) vendorsRepository.findAll()
                .stream().filter(v -> v.isActive()).count(); // Active vendors count

        // Construct and return the dashboard DTO
        return new AdminDashboardResponse(admin, totalVendors, activeVendors);
    }
}
