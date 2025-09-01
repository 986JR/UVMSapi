package com.uvms.apiuvms.controller;

import com.uvms.apiuvms.entity.Admins;
import com.uvms.apiuvms.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/admins")
public class AdminController {
    @Autowired
    private AdminService adminService;

    //Get
    @GetMapping
    public List<Admins> getAllAdmins(){
        return adminService.getAllAdmins();
    }

    //Get by Id
    @GetMapping("/{admin_id}")
    public ResponseEntity<Admins> getAdminsById(@PathVariable Integer admin_id) {
        Optional<Admins> admins = adminService.getAdminsById(admin_id);
        return admins
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }

    //Post
    @PostMapping
    public void postAdmin(@RequestBody Admins admins) {
        adminService.addAdmin(admins);
    }

    //Deleting an Admin by Id
    @DeleteMapping("/{admin_id}")
    public void deleteAdmin(@PathVariable Integer admin_id){
        adminService.deleteAdminById(admin_id);
    }

    //Putting An Admin
    @PutMapping("/{admin_id}")
    public ResponseEntity<Admins> putAdminById(@PathVariable Integer admin_id
                                              , @RequestBody Admins adminsDetails ) {

        Optional<Admins> adminsOptional = adminService.getAdminsById(admin_id);

        if(adminsOptional.isEmpty()) {
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
           //admins.setCreated_at(adminsDetails.getCreated_at());

        Admins updatedAdmins = adminService.addAdmin(admins);
        return ResponseEntity.ok(updatedAdmins);

    }
 }
