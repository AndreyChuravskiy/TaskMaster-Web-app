package org.example.taskmasterwebapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.taskmasterwebapp.dto.JwtRequest;
import org.example.taskmasterwebapp.dto.JwtResponse;
import org.example.taskmasterwebapp.dto.RefreshJwtRequest;
import org.example.taskmasterwebapp.dto.RegisterRequest;
import org.example.taskmasterwebapp.exception.AuthException;
import org.example.taskmasterwebapp.service.AuthService;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping("/access_token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {

        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}
