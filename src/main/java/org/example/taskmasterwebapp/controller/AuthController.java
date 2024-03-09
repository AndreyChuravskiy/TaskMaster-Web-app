package org.example.taskmasterwebapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.taskmasterwebapp.dto.JwtRequest;
import org.example.taskmasterwebapp.dto.JwtResponse;
import org.example.taskmasterwebapp.dto.RegisterRequest;
import org.example.taskmasterwebapp.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
            final JwtResponse token = authService.login(authRequest);
            return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest registerRequest) {
        final JwtResponse token = authService.register(registerRequest);
        return ResponseEntity.ok(token);
    }

}
