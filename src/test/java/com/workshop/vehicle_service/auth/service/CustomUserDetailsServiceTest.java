package com.workshop.vehicle_service.auth.service;

import com.workshop.vehicle_service.auth.entity.Utilisateur;
import com.workshop.vehicle_service.auth.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void testLoadUserByUsername_UserExists() {
        // Arrange
        Utilisateur mockUser = new Utilisateur();
        mockUser.setUsername("existUser");
        when(utilisateurRepository.findByUsername("existUser")).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails result = customUserDetailsService.loadUserByUsername("existUser");

        // Assert
        assertNotNull(result);
        assertEquals("existUser", result.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserDoesNotExist() {
        // Arrange
        when(utilisateurRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("unknownUser");
        });
    }
}
