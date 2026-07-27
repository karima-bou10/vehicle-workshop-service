package com.workshop.vehicle_service.auth.service.impl;

import com.workshop.vehicle_service.auth.dto.LoginRequest;
import com.workshop.vehicle_service.auth.dto.LoginResponse;
import com.workshop.vehicle_service.auth.dto.UtilisateurResponse;
import com.workshop.vehicle_service.auth.service.AuthService;
import com.workshop.vehicle_service.auth.service.CustomUserDetailsService;
import com.workshop.vehicle_service.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            // vérifier les usernames (Spring s'occupe de comparer le mot de passe avec BCrypt)
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            throw new BadCredentialsException("username ou mot de passe incorrect");
        }

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String jwtToken = jwtService.generateToken(user);

        return new LoginResponse(jwtToken, "Bearer", jwtService.extractExpiration(jwtToken));
    }

    @Override
    public UtilisateurResponse getCurrentUser() {

        // récupèrer l'utilisateur connecté depuis le contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            return new UtilisateurResponse(userDetails.getUsername(), role);
        }
        return new UtilisateurResponse();
    }
}
