package com.uvms.apiuvms.service;

import com.uvms.apiuvms.entity.Admins;
import com.uvms.apiuvms.repository.AdminsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminsRepository adminsRepository;

    //CRUD
    //Getting All Admins
    public List<Admins> getAllAdmins() {
        return adminsRepository.findAll();
    }

    //Getting All Admins By Id
    public Optional<Admins> getAdminsById(Integer id) {
        return adminsRepository.findById(id);
    }

    //Adding an Admin
    public Admins addAdmin(Admins admins) {
        return (Admins) adminsRepository.save(admins);
    }

    //Deleting An Admin
    public void deleteAdminById(Integer id) {
        adminsRepository.deleteById(id);
    }
}
