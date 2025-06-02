package com.example.twelvefactorapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class DemoSecurityController {

    @GetMapping("/public/hello")
    public ResponseEntity<String> getPublicHello() {
        return ResponseEntity.ok("Hello from public endpoint!");
    }

    @GetMapping("/admin/info")
    public ResponseEntity<String> getAdminInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(", "));
        return ResponseEntity.ok("Hello, " + authentication.getName() + "! You have roles: " + roles + ". This is admin info.");
    }

    @GetMapping("/secure/data")
    public ResponseEntity<String> getSecureData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(", "));
        return ResponseEntity.ok("Hello, " + authentication.getName() + "! You have roles: " + roles + ". This is secure data for any authenticated user.");
    }

    // Existing /hello endpoint from previous subtask, should now be secured
    @GetMapping("/hello")
    public String hello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "Hello, " + authentication.getName() + "! (This is the original /hello endpoint, now secured)";
    }
}
