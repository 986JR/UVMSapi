package com.uvms.apiuvms.service;

import com.uvms.apiuvms.entity.RenewalRequests;
import com.uvms.apiuvms.repository.RenewalRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RenewalRequestService {

    @Autowired
    private RenewalRequestRepository renewalRequestRepository;

    public List<RenewalRequests> getAllRenewalRequests() {
        return renewalRequestRepository.findAll();
    }

    public Optional<RenewalRequests> getRenewalRequestById(Integer id) {
        return renewalRequestRepository.findById(id);
    }

    public RenewalRequests saveRenewalRequest(RenewalRequests renewalRequest) {
        return renewalRequestRepository.save(renewalRequest);
    }

    public void deleteRenewalRequestById(Integer id) {
        renewalRequestRepository.deleteById(id);
    }

    // Additional methods for business logic
    public List<RenewalRequests> getRenewalRequestsByStatus(RenewalRequests.Status status) {
        return renewalRequestRepository.findAll().stream()
                .filter(request -> request.getStatus() == status)
                .toList();
    }
}