package org.example.taskmasterwebapp.service;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.taskmasterwebapp.domain.User;
import org.example.taskmasterwebapp.dto.JwtAuthentication;
import org.example.taskmasterwebapp.dto.JwtRequest;
import org.example.taskmasterwebapp.dto.JwtResponse;
import org.example.taskmasterwebapp.dto.RegisterRequest;
import org.example.taskmasterwebapp.exception.AuthException;
import org.example.taskmasterwebapp.exception.UserAlreadyExistsException;
import org.example.taskmasterwebapp.repository.RefreshStorageRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final RefreshStorageUtils refreshStorageUtils;

    //private final Map<String, String> refreshStorage = new HashMap<>(); //TODO: database

    private final JwtProvider jwtProvider;

    public JwtResponse login(@NonNull JwtRequest authRequest){
        final User user = userService.findUserByUsername(authRequest.getLogin())
                .orElseThrow(() -> new AuthException("User not found"));

        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorageUtils.put(user.getUsername(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Wrong password");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = refreshStorageUtils.get(username);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {

                final User user = userService.findUserByUsername(username)
                        .orElseThrow(() -> new AuthException("User not found"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        throw new AuthException("Invalid jwt token");
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {

            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = refreshStorageUtils.get(username);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.findUserByUsername(username)
                        .orElseThrow(() -> new AuthException("User not found"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorageUtils.put(user.getUsername(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Invalid jwt token");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    public JwtResponse register(RegisterRequest registerRequest) {
        if (userService.findUserByUsername(registerRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("A user with the same username or email already exists.");
        }

        List <String> roles = registerRequest.getUserRoles();

        userService.createUser(registerRequest.getUsername(), registerRequest.getPassword(),
                roles.toArray(new String[0]));  //TODO: cheeeeck
        JwtRequest loginRequest = new JwtRequest(registerRequest.getUsername(), registerRequest.getPassword());

        return login(loginRequest);
    }
}
