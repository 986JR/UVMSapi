package com.uvms.apiuvms.service;

import com.uvms.apiuvms.entity.Colleges;
import com.uvms.apiuvms.repository.CollegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.codec.ResourceEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
public class CollegeService {

    @Autowired
    private CollegeRepository collegeRepository;

    //CRUD
    //Getting All colleges
    public List<Colleges> getAllcolleges() {
        return collegeRepository.findAll();
    }

    //GEtting Colleges By ID
    public Optional<Colleges> getCollegeById(Integer id) {
        return collegeRepository.findById(id);
    }

    //Adding A College
    public Colleges saveCollege(Colleges colleges) {

        return (Colleges) collegeRepository.save(colleges);
    }

    //Deleting a College
    public void deleteCollegeById(Integer id) {
        collegeRepository.deleteById(id);
    }
}
