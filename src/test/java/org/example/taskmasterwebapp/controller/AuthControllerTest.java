package org.example.taskmasterwebapp.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.taskmasterwebapp.dto.*;
import org.example.taskmasterwebapp.service.AuthService;
import org.example.taskmasterwebapp.service.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    private final String ACCESS_TOKEN = "lyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwZXRpYSIsImV4cCI6MTcxMDg4ODUyNiwicm9sZXMiOlsiRVhFQ1VUT1IiXSwidXNlcm5hbWUiOiJwZXRpYSJ9.uBdjGHr-Qfr2_qInsGHbh6pKPuENiBv5B3HgOhlDSmD1Iu7E5EZH8LRMfvRErAoTgJ3Qa-vm7AjmeQBd5nrCNA";
    private final String REFRESH_TOKEN = "lyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwZXRpYSIsImV4cCI6MTcxMzMwMDUyNn0.ZEOf5vXLosnMMOLw4BBSybicJD85o6SdTbXXbxolNgM-lqdcyth4hLzOlhwLxDd4F2GAhmCo8UwgQM1npw2syg";

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login() throws Exception{
        JwtRequest authRequest = new JwtRequest("andrey", "123456");

        String jwtRequestJson = objectMapper.writeValueAsString(authRequest);
        JwtResponse jwtResponse = new JwtResponse(ACCESS_TOKEN, REFRESH_TOKEN);

        when(authService.login(any(JwtRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jwtRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(REFRESH_TOKEN));

        verify(authService, times(1)).login(any(JwtRequest.class));
    }

    @Test
    void register() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("andrey",
                "123456",
                List.of("ISSUER", "EXECUTOR"));
        String registerRequestJson = objectMapper.writeValueAsString(registerRequest);
        JwtResponse jwtResponse = new JwtResponse(ACCESS_TOKEN, REFRESH_TOKEN);

        when(authService.register(any(RegisterRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(REFRESH_TOKEN));

        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void getNewAccessToken() throws Exception {
        RefreshJwtRequest refreshJwtRequest = new RefreshJwtRequest(REFRESH_TOKEN);
        String refreshJwtRequestJson = objectMapper.writeValueAsString(refreshJwtRequest);
        JwtResponse jwtResponse = new JwtResponse(ACCESS_TOKEN, null);

        when(authService.getAccessToken(any(String.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/api/auth/access_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshJwtRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN))
                .andExpect(jsonPath("$.refreshToken").isEmpty());

        verify(authService, times(1)).getAccessToken(any(String.class));
    }

    @Test
    void getNewRefreshToken() throws Exception{
        RefreshJwtRequest refreshJwtRequest = new RefreshJwtRequest(REFRESH_TOKEN);
        String refreshJwtRequestJson = objectMapper.writeValueAsString(refreshJwtRequest);
        JwtResponse jwtResponse = new JwtResponse(ACCESS_TOKEN, REFRESH_TOKEN);

        when(authService.refresh(any(String.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/api/auth/refresh_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshJwtRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(REFRESH_TOKEN));

        verify(authService, times(1)).refresh(any(String.class));
    }
}
