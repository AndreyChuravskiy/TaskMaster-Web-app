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
    @PreAuthorize("hasAuthority('EXECUTOR')")
    @GetMapping("executor")
    public ResponseEntity<String> helloExecutor() {
        String name = (String)authService.getAuthInfo().getPrincipal();
        return ResponseEntity.ok("hello, executor " + name);
    }

    @PreAuthorize("hasAuthority('ISSUER')")
    @GetMapping("issuer")
    public ResponseEntity<String> helloIssuer() {
        String name = (String)authService.getAuthInfo().getPrincipal();
        return ResponseEntity.ok("hello, issuer " + name);
    }

}
