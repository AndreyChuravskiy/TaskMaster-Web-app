package org.example.taskmasterwebapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskmasterwebapp.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/hello")
public class HelloController {

    private final AuthService authService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("admin")
    public ResponseEntity<String> helloAdmin() {
        String name = (String)authService.getAuthInfo().getPrincipal();
        return ResponseEntity.ok("hello, admin " + name);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("user")
    public ResponseEntity<String> helloUser() {
        String name = (String)authService.getAuthInfo().getPrincipal();
        return ResponseEntity.ok("hello, user " + name);
    }

}
