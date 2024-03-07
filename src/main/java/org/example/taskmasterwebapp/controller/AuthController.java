package org.example.taskmasterwebapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.taskmasterwebapp.dto.JwtRequest;
import org.example.taskmasterwebapp.dto.JwtResponse;
import org.example.taskmasterwebapp.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public String login(@RequestBody JwtRequest authRequest) {
            final JwtResponse token = authService.login(authRequest);
            return token.getAccessToken() + token.getRefreshToken();
    }

    @GetMapping("register")
    public String register() {
        //final JwtResponse token = authService.register(username, password, userRoles);
        return "token.getAccessToken() + token.getRefreshToken()";
    }

}
