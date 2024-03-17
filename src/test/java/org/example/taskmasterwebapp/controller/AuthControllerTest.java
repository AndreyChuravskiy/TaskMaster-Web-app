package org.example.taskmasterwebapp.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.example.taskmasterwebapp.dto.JwtAuthentication;
import org.example.taskmasterwebapp.dto.JwtRequest;
import org.example.taskmasterwebapp.dto.JwtResponse;
import org.example.taskmasterwebapp.filter.JwtFilter;
import org.example.taskmasterwebapp.service.AuthService;
import org.example.taskmasterwebapp.service.JwtProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
//@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

//    @Mock
//    private AuthService authService;

//    @InjectMocks
//    private AuthController authController;
    @MockBean
    private AuthService authService;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
//        objectMapper = new ObjectMapper();
//    }

    @Test
    void login() throws Exception{
//        Assertions.assertEquals(25, 25);
        JwtRequest authRequest = new JwtRequest("andrey", "123456");

        String jwtRequestJson = objectMapper.writeValueAsString(authRequest);
        JwtResponse jwtResponse = new JwtResponse(
                "qyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwZXRpYSIsImV4cCI6MTcxMDg4ODUyNiwicm9sZXMiOlsiRVhFQ1VUT1IiXSwidXNlcm5hbWUiOiJwZXRpYSJ9.uBdjGHr-Qfr2_qInsGHbh6pKPuENiBv5B3HgOhlDSmD1Iu7E5EZH8LRMfvRErAoTgJ3Qa-vm7AjmeQBd5nrCNA",
                "qyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwZXRpYSIsImV4cCI6MTcxMzMwMDUyNn0.ZEOf5vXLosnMMOLw4BBSybicJD85o6SdTbXXbxolNgM-lqdcyth4hLzOlhwLxDd4F2GAhmCo8UwgQM1npw2syg");

        when(authService.login(any(JwtRequest.class))).thenReturn(jwtResponse);

        JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setAuthenticated(true);
        jwtAuthentication.setUsername("andrey");

        doAnswer(invocation -> {
            ServletRequest request = invocation.getArgument(0);
            ServletResponse response = invocation.getArgument(1);
            FilterChain fc = invocation.getArgument(2);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
            fc.doFilter(request, response);
            return null;
        }).when(jwtFilter).doFilter(any(ServletRequest.class), any(ServletResponse.class), any(FilterChain.class));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jwtRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").value("qyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwZXRpYSIsImV4cCI6MTcxMDg4ODUyNiwicm9sZXMiOlsiRVhFQ1VUT1IiXSwidXNlcm5hbWUiOiJwZXRpYSJ9.uBdjGHr-Qfr2_qInsGHbh6pKPuENiBv5B3HgOhlDSmD1Iu7E5EZH8LRMfvRErAoTgJ3Qa-vm7AjmeQBd5nrCNA"))
                .andExpect(jsonPath("$.refreshToken").value("qyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwZXRpYSIsImV4cCI6MTcxMzMwMDUyNn0.ZEOf5vXLosnMMOLw4BBSybicJD85o6SdTbXXbxolNgM-lqdcyth4hLzOlhwLxDd4F2GAhmCo8UwgQM1npw2syg"));

        verify(authService, times(1)).login(any(JwtRequest.class));
    }
}
