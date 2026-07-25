package com.workshop.vehicle_service.auth.service;

import com.workshop.vehicle_service.auth.dto.LoginRequest;
import com.workshop.vehicle_service.auth.dto.LoginResponse;
import com.workshop.vehicle_service.auth.dto.UtilisateurResponse;
import com.workshop.vehicle_service.auth.entity.Utilisateur;
import com.workshop.vehicle_service.auth.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @AfterEach
    void tearDown() {
        // Nettoyer le contexte de sécurité après chaque test
        SecurityContextHolder.clearContext();
    }

    @Test
    void testLogin_Success() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("password");

        Utilisateur mockUser = new Utilisateur();
        mockUser.setUsername("admin");
        mockUser.setRole("ADMIN");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userDetailsService.loadUserByUsername("admin")).thenReturn(mockUser);
        when(jwtService.generateToken(mockUser)).thenReturn("fake-jwt-token");
        when(jwtService.extractExpiration("fake-jwt-token")).thenReturn(new Date());

        // Act
        LoginResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("Bearer", response.getType());
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void testLogin_BadCredentialsThrowsException() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrong-password");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Bad credentials"));

        // Act & Assert
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authService.login(request);
        });
        assertEquals("username ou mot de passe incorrect", exception.getMessage());
    }

    @Test
    void testGetCurrentUser_Authenticated() {
        // Arrange : Simuler le SecurityContextHolder
        Utilisateur mockUser = new Utilisateur();
        mockUser.setUsername("admin");
        mockUser.setRole("ROLE_ADMIN");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        UtilisateurResponse response = authService.getCurrentUser();

        // Assert
        assertNotNull(response);
        assertEquals("admin", response.getUsername());
    }

    @Test
    void testGetCurrentUser_NotAuthenticated() {
        // Arrange
        SecurityContextHolder.clearContext();

        // Act
        UtilisateurResponse response = authService.getCurrentUser();

        // Assert
        assertNull(response.getUsername());
    }
}

