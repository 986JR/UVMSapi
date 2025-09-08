package com.uvms.apiuvms.controller;

import com.uvms.apiuvms.dto.AdminDashboardResponse;
import com.uvms.apiuvms.entity.Admins;
import com.uvms.apiuvms.security.JwtUtil;
import com.uvms.apiuvms.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    // Get all admins
    @GetMapping
    public List<Admins> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    // Get admin by ID
    @GetMapping("/{admin_id}")
    public ResponseEntity<Admins> getAdminsById(@PathVariable Integer admin_id) {
        Optional<Admins> admins = adminService.getAdminsById(admin_id);
        return admins.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Add new admin
    @PostMapping
    public void postAdmin(@RequestBody Admins admins) {
        adminService.addAdmin(admins);
    }

    // Update admin
    @PutMapping("/{admin_id}")
    public ResponseEntity<Admins> putAdminById(@PathVariable Integer admin_id,
                                               @RequestBody Admins adminsDetails) {
        Optional<Admins> adminsOptional = adminService.getAdminsById(admin_id);

        if (adminsOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Admins admins = adminsOptional.get();
        admins.setColleges(adminsDetails.getColleges());
        admins.setEmail(adminsDetails.getEmail());
        admins.setIs_active(adminsDetails.isIs_active());
        admins.setLast_login(adminsDetails.getLast_login());
        admins.setPassword_hash(adminsDetails.getPassword_hash());
        admins.setRole(adminsDetails.getRole());
        admins.setName(adminsDetails.getName());

        Admins updatedAdmins = adminService.addAdmin(admins);
        return ResponseEntity.ok(updatedAdmins);
    }

    // Delete admin
    @DeleteMapping("/{admin_id}")
    public void deleteAdmin(@PathVariable Integer admin_id) {
        adminService.deleteAdminById(admin_id);
    }

    /**
     * GET /api/admins/dashboard
     * Secured endpoint for admin dashboard
     * Requires JWT token in Authorization header: "Bearer <token>"
     */
    @GetMapping("/dashboard")
    public ResponseEntity<?> getAdminDashboard(HttpServletRequest request) {
        try {
            // Extract Authorization header
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || authHeader.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing Authorization header");
            }

            // Extract JWT token
            String token = jwtUtil.extractTokenFromHeader(authHeader);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid Authorization header format. Use: Bearer <token>");
            }

            // Validate token
            if (!jwtUtil.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid or expired JWT token");
            }

            // Extract admin ID from token
            Integer adminId = jwtUtil.extractAdminId(token);
            if (adminId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid JWT token: missing admin ID");
            }

            // Get dashboard data from service
            AdminDashboardResponse dashboardData = adminService.getDashboardDataById(adminId);

            return ResponseEntity.ok(dashboardData);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error occurred");
        }
    }
}
