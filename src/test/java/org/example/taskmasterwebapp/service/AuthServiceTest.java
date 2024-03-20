package org.example.taskmasterwebapp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import org.example.taskmasterwebapp.domain.User;
import org.example.taskmasterwebapp.domain.UserRole;
import org.example.taskmasterwebapp.dto.JwtRequest;
import org.example.taskmasterwebapp.dto.JwtResponse;
import org.example.taskmasterwebapp.dto.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    private final String JWT_REFRESH_SECRET = "sQDoRC38cVSWEHfqVUZCV9SDbUb3dEJ9+NHzloUaPDKuJWXv+Wplt4ib1Lr1nsnGQpRPM2P19vDqLw/sDFaU7A==";

    private final String JWT_ACCESS_SECRET = "M5ZQKVbNqCd4HDhQ7HsKESXqHpnljjv7R9s7RJtN3hqhC/bI0lJYwaKrvWwMhIn93RdvqjJGUn17PBrKiiZuyQ==";

    private String refreshToken;

    private String accessToken;

    private User user;

    @Mock
    private UserService userService;

    @Mock
    private RefreshStorageUtils refreshStorageUtils;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    private String generateRefreshToken(@NonNull User user) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_REFRESH_SECRET));
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(refreshExpiration)
                .signWith(key)
                .compact();
    }

    public String generateAccessToken(@NonNull User user) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_ACCESS_SECRET));
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(refreshExpiration)
                .signWith(key)
                .compact();
    }

    @BeforeEach
    void setUp() {
        user = new User(2, "andrey", "12345", Set.of(new UserRole("EXECUTOR") ,new UserRole("ISSUER")));
        accessToken = generateAccessToken(user);
        refreshToken = generateRefreshToken(user);
    }

    @Test
    void login() {
        JwtRequest authRequest = new JwtRequest("andrey", "12345");
        JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken);
        Optional<User> optionalUser = Optional.of(user);

        when(userService.findUserByUsername(any(String.class))).thenReturn(optionalUser);
        when(jwtProvider.generateAccessToken(any(User.class))).thenReturn(accessToken);
        when(jwtProvider.generateRefreshToken(any(User.class))).thenReturn(refreshToken);

        JwtResponse returnedJwtResponse = authService.login(authRequest);
        Assertions.assertEquals(returnedJwtResponse, jwtResponse);

        verify(userService, times(1)).findUserByUsername(any(String.class));
        verify(jwtProvider, times(1)).generateAccessToken(any(User.class));
        verify(jwtProvider, times(1)).generateRefreshToken(any(User.class));
    }

    @Test
    void getAccessToken() {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_REFRESH_SECRET));
        Optional<User> optionalUser = Optional.of(user);
        JwtResponse jwtResponse = new JwtResponse(accessToken, null);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();

        when(jwtProvider.validateRefreshToken(any(String.class))).thenReturn(true);
        when(jwtProvider.getRefreshClaims(any(String.class))).thenReturn(claims);
        when(refreshStorageUtils.get(any(String.class))).thenReturn(refreshToken);
        when(userService.findUserByUsername(any(String.class))).thenReturn(optionalUser);
        when(jwtProvider.generateAccessToken(any(User.class))).thenReturn(accessToken);

        JwtResponse returnedJwtResponse = authService.getAccessToken(refreshToken);
        Assertions.assertEquals(returnedJwtResponse, jwtResponse);

        verify(jwtProvider, times(1)).generateAccessToken(any(User.class));
        verify(userService, times(1)).findUserByUsername(any(String.class));
        verify(jwtProvider, times(1)).validateRefreshToken(any(String.class));
        verify(jwtProvider, times(1)).getRefreshClaims(any(String.class));
        verify(refreshStorageUtils, times(1)).get(any(String.class));
    }

    @Test
    void refresh() {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_REFRESH_SECRET));
        Optional<User> optionalUser = Optional.of(user);
        JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();

        when(jwtProvider.validateRefreshToken(any(String.class))).thenReturn(true);
        when(jwtProvider.getRefreshClaims(any(String.class))).thenReturn(claims);
        when(refreshStorageUtils.get(any(String.class))).thenReturn(refreshToken);
        when(userService.findUserByUsername(any(String.class))).thenReturn(optionalUser);
        when(jwtProvider.generateAccessToken(any(User.class))).thenReturn(accessToken);
        when(jwtProvider.generateRefreshToken(any(User.class))).thenReturn(refreshToken);
        doNothing().when(refreshStorageUtils).put(any(String.class), any(String.class));

        JwtResponse returnedJwtResponse = authService.refresh(refreshToken);
        Assertions.assertEquals(returnedJwtResponse, jwtResponse);

        verify(jwtProvider, times(1)).generateAccessToken(any(User.class));
        verify(jwtProvider, times(1)).generateRefreshToken(any(User.class));
        verify(userService, times(1)).findUserByUsername(any(String.class));
        verify(jwtProvider, times(1)).validateRefreshToken(any(String.class));
        verify(jwtProvider, times(1)).getRefreshClaims(any(String.class));
        verify(refreshStorageUtils, times(1)).get(any(String.class));
        verify(refreshStorageUtils, times(1)).put(any(String.class), any(String.class));
    }

    @Test
    void register() {
        RegisterRequest registerRequest = new RegisterRequest("andrey", "12345", List.of("EXECUTOR", "ISSUER"));
        Optional<User> optionalUserEmpty = Optional.empty();
        Optional<User> optionalUser = Optional.of(user);
        JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken);

        when(userService.findUserByUsername(registerRequest.getUsername())).thenReturn(optionalUserEmpty)
                .thenReturn(optionalUser);
        doNothing().when(userService).createUser(any(String.class), any(String.class), any(String[].class));
        doNothing().when(refreshStorageUtils).put(any(String.class), any(String.class));
        when(jwtProvider.generateAccessToken(any(User.class))).thenReturn(accessToken);
        when(jwtProvider.generateRefreshToken(any(User.class))).thenReturn(refreshToken);

        JwtResponse returnedJwtResponse = authService.register(registerRequest);
        Assertions.assertEquals(returnedJwtResponse, jwtResponse);

        verify(jwtProvider, times(1)).generateAccessToken(any(User.class));
        verify(jwtProvider, times(1)).generateRefreshToken(any(User.class));
        verify(refreshStorageUtils, times(1)).put(any(String.class), any(String.class));
        verify(userService, times(2)).findUserByUsername(any(String.class));
        verify(userService, times(1)).createUser(any(String.class), any(String.class), any(String[].class));
    }
}
