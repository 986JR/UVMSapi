package com.uvms.apiuvms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

@RestController
public class ProtectedController {

    @GetMapping("/api/dashboard")
    public ResponseEntity<?> dashboard(Authentication authentication) {
        return ResponseEntity.ok(Map.of(
                "username", authentication.getName(),
                "message", "You have accessed a protected resource"
        ));
    }

}
