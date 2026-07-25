package com.workshop.vehicle_service.auth.service;

import com.workshop.vehicle_service.auth.entity.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {
    private JwtService jwtService;
    private Utilisateur mockUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        // Injection manuelle des propriétés @Value
        // Clé secrète de 256 bits (minimum requis par HS256) encodée en Base64
        ReflectionTestUtils.setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 1000 * 60 * 60); // 1 heure

        mockUser = new Utilisateur();
        mockUser.setUsername("testUser");
        mockUser.setRole("ROLE_USER");
    }

    @Test
    void testGenerateTokenAndExtractUsername() {
        // Act
        String token = jwtService.generateToken(mockUser);
        String extractedUsername = jwtService.extractUsername(token);

        // Assert
        assertNotNull(token);
        assertEquals("testUser", extractedUsername);
    }

    @Test
    void testGenerateTokenAndExtractExpiration() {
        // Act
        String token = jwtService.generateToken(mockUser);
        Date extractedExpiration = jwtService.extractExpiration(token);

        // Assert
        assertNotNull(token);
        long expectedTimeMillis = System.currentTimeMillis() + (1000 * 60 * 60); // 1 heure en millisecondes
        long actualTimeMillis = extractedExpiration.getTime();
        // Vérifier que la différence entre les deux dates est de moins de 2 secondes (2000 ms)
        long difference = Math.abs(expectedTimeMillis - actualTimeMillis);
        assertTrue(difference < 2000, "La date d'expiration doit être à environ 1 heure de l'heure actuelle. Différence actuelle : " + difference + " ms");
    }

    @Test
    void testIsTokenValid() {
        // Act
        String token = jwtService.generateToken(mockUser);
        boolean isValid = jwtService.isTokenValid(token, mockUser);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testIsTokenExpired() {
        // Act
        String token = jwtService.generateToken(mockUser);
        boolean isExpired = jwtService.isTokenExpired(token);

        // Assert
        assertFalse(isExpired);
    }

    @Test
    void testIsTokenInvalidForDifferentUser() {
        // Arrange
        String token = jwtService.generateToken(mockUser);

        Utilisateur otherUser = new Utilisateur();
        otherUser.setUsername("hacker");

        // Act
        boolean isValid = jwtService.isTokenValid(token, otherUser);

        // Assert
        assertFalse(isValid);
    }
}
