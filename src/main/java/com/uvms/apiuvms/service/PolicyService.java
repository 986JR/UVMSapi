package com.uvms.apiuvms.service;

import com.uvms.apiuvms.entity.Policies;
import com.uvms.apiuvms.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    public List<Policies> getAllPolicies() {
        return policyRepository.findAll();
    }

    public Optional<Policies> getPolicyById(Integer id) {
        return policyRepository.findById(id);
    }

    public Policies savePolicy(Policies policy) {
        return policyRepository.save(policy);
    }

    public void deletePolicyById(Integer id) {
        policyRepository.deleteById(id);
    }

    // Additional methods for business logic
    public List<Policies> getActivePolicies() {
        return policyRepository.findAll().stream()
                .filter(Policies::getIsActive)
                .toList();
    }

    public List<Policies> getPoliciesByScope(Policies.Scope scope) {
        return policyRepository.findAll().stream()
                .filter(policy -> policy.getScope() == scope)
                .toList();
    }
}