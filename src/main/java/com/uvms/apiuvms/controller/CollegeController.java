package com.uvms.apiuvms.controller;

import com.uvms.apiuvms.entity.Colleges;
import com.uvms.apiuvms.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/colleges")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    //Getting All Colleges
    @GetMapping
    public List<Colleges> getAllColleges() {
        return collegeService.getAllcolleges();
    }

    //Getting All Colleges By Id
    @GetMapping("/{college_id}")
    public ResponseEntity<Colleges> getCollegeById(@PathVariable Integer college_id) {
        Optional<Colleges> colleges= collegeService.getCollegeById(college_id);
        return colleges
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }

    //Deleting A college By Id
    @DeleteMapping("/{college_id}")
    public void deleteCollegeById(@PathVariable Integer college_id) {
        collegeService.deleteCollegeById(college_id);
    }

    //Posting A College
    @PostMapping
   public void postCollege(@RequestBody Colleges colleges) {
        collegeService.saveCollege(colleges);
    }

    //Putting a Collage By Id
    @PutMapping("/{college_id}")
    public ResponseEntity<Colleges> putCollegeById(@PathVariable Integer college_id
            , @RequestBody Colleges collegesDetails) {

        Optional<Colleges> collegesOptional = collegeService.getCollegeById(college_id);

        if(collegesOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Colleges colleges=collegesOptional.get();
           colleges.setCollege_name(collegesDetails.getCollege_name());

           Colleges updatedCollege = collegeService.saveCollege(colleges);

           return ResponseEntity.ok(updatedCollege);
    }
}
