package com.uvms.apiuvms.controller;

import com.uvms.apiuvms.entity.RenewalRequests;
import com.uvms.apiuvms.service.RenewalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/renewal-requests")
public class RenewalRequestController {

    @Autowired
    private RenewalRequestService renewalRequestService;

    @GetMapping
    public List<RenewalRequests> getAllRenewalRequests() {
        return renewalRequestService.getAllRenewalRequests();
    }

    @GetMapping("/status/{status}")
    public List<RenewalRequests> getRenewalRequestsByStatus(@PathVariable RenewalRequests.Status status) {
        return renewalRequestService.getRenewalRequestsByStatus(status);
    }

    @GetMapping("/{renewal_id}")
    public ResponseEntity<RenewalRequests> getRenewalRequestById(@PathVariable Integer renewal_id) {
        Optional<RenewalRequests> renewalRequest = renewalRequestService.getRenewalRequestById(renewal_id);
        return renewalRequest.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public RenewalRequests createRenewalRequest(@RequestBody RenewalRequests renewalRequest) {
        return renewalRequestService.saveRenewalRequest(renewalRequest);
    }

    @PutMapping("/{renewal_id}")
    public ResponseEntity<RenewalRequests> updateRenewalRequest(@PathVariable Integer renewal_id,
                                                                @RequestBody RenewalRequests renewalRequestDetails) {
        Optional<RenewalRequests> renewalRequestOptional = renewalRequestService.getRenewalRequestById(renewal_id);

        if (renewalRequestOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RenewalRequests renewalRequest = renewalRequestOptional.get();
        renewalRequest.setStatus(renewalRequestDetails.getStatus());

        RenewalRequests updatedRenewalRequest = renewalRequestService.saveRenewalRequest(renewalRequest);
        return ResponseEntity.ok(updatedRenewalRequest);
    }

    @DeleteMapping("/{renewal_id}")
    public void deleteRenewalRequest(@PathVariable Integer renewal_id) {
        renewalRequestService.deleteRenewalRequestById(renewal_id);
    }
}