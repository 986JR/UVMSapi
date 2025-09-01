package com.uvms.apiuvms.controller;

import com.uvms.apiuvms.entity.Policies;
import com.uvms.apiuvms.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = "https://wl9ngccd-8080.euw.devtunnels.ms")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    @GetMapping
    public List<Policies> getAllPolicies() {
        return policyService.getAllPolicies();
    }

    @GetMapping("/active")
    public List<Policies> getActivePolicies() {
        return policyService.getActivePolicies();
    }

    @GetMapping("/scope/{scope}")
    public List<Policies> getPoliciesByScope(@PathVariable Policies.Scope scope) {
        return policyService.getPoliciesByScope(scope);
    }

    @GetMapping("/{policy_id}")
    public ResponseEntity<Policies> getPolicyById(@PathVariable Integer policy_id) {
        Optional<Policies> policy = policyService.getPolicyById(policy_id);
        return policy.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Policies createPolicy(@RequestBody Policies policy) {
        return policyService.savePolicy(policy);
    }

    @PutMapping("/{policy_id}")
    public ResponseEntity<Policies> updatePolicy(@PathVariable Integer policy_id,
                                                 @RequestBody Policies policyDetails) {
        Optional<Policies> policyOptional = policyService.getPolicyById(policy_id);

        if (policyOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Policies policy = policyOptional.get();
        policy.setTitle(policyDetails.getTitle());
        policy.setContent(policyDetails.getContent());
        policy.setScope(policyDetails.getScope());
        policy.setCollege(policyDetails.getCollege());
        policy.setIsActive(policyDetails.getIsActive());

        Policies updatedPolicy = policyService.savePolicy(policy);
        return ResponseEntity.ok(updatedPolicy);
    }

    @DeleteMapping("/{policy_id}")
    public void deletePolicy(@PathVariable Integer policy_id) {
        policyService.deletePolicyById(policy_id);
    }
}