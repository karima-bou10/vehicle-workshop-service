package com.workshop.vehicle_service.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.vehicle_service.auth.dto.LoginRequest;
import com.workshop.vehicle_service.auth.dto.LoginResponse;
import com.workshop.vehicle_service.auth.dto.UtilisateurResponse;
import com.workshop.vehicle_service.auth.service.AuthService;
import com.workshop.vehicle_service.auth.config.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Désactive les filtres de sécurité globaux pour isoler le test du contrôleur
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    public void testLogin_Success() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("password123");

        LoginResponse response = new LoginResponse("mocked-jwt-token", "Bearer", new Date());
        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    public void testLogin_ValidationFailed() throws Exception {
        // Arrange : Mot de passe trop court pour déclencher @Valid
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("123");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Code 400 attendu
    }

    @Test
    public void testGetCurrentUser_Success() throws Exception {
        // Arrange
        UtilisateurResponse response = new UtilisateurResponse("admin", "[ROLE_MANAGER]");
        when(authService.getCurrentUser()).thenReturn(response); // simuler user connecté pour eviter de devoir generer un vrai JWT pour le test

        // Act & Assert
        mockMvc.perform(get("/api/auth/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.role").value("[ROLE_MANAGER]"));
    }
}
