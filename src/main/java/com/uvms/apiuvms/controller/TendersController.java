// TendersController.java in controller folder
package com.uvms.apiuvms.controller;

import com.uvms.apiuvms.entity.Tenders;
import com.uvms.apiuvms.service.TendersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tenders")
public class TendersController {

    @Autowired
    private TendersService tendersService;

    @GetMapping
    public List<Tenders> getAllTenders() {
        return tendersService.getAllTenders();
    }

    @GetMapping("/{tender_id}")
    public ResponseEntity<Tenders> getTenderById(@PathVariable Integer tender_id) {
        Optional<Tenders> tender = tendersService.getTenderById(tender_id);
        return tender.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Tenders createTender(@RequestBody Tenders tender) {
        return tendersService.saveTender(tender);
    }

    @PutMapping("/{tender_id}")
    public ResponseEntity<Tenders> updateTender(@PathVariable Integer tender_id,
                                                @RequestBody Tenders tenderDetails) {
        Optional<Tenders> tenderOptional = tendersService.getTenderById(tender_id);

        if (tenderOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Tenders tender = tenderOptional.get();
        // Update fields as needed
        tender.setTitle(tenderDetails.getTitle());
        tender.setDescription(tenderDetails.getDescription());
        tender.setDeadlineDate(tenderDetails.getDeadlineDate());
        tender.setStatus(tenderDetails.getStatus());
        tender.setContractTemplatePath(tenderDetails.getContractTemplatePath());

        Tenders updatedTender = tendersService.saveTender(tender);
        return ResponseEntity.ok(updatedTender);
    }

    @DeleteMapping("/{tender_id}")
    public void deleteTender(@PathVariable Integer tender_id) {
        tendersService.deleteTenderById(tender_id);
    }
}