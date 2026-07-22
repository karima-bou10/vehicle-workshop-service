package com.workshop.vehicle_service.auth.controller;

import com.workshop.vehicle_service.auth.dto.LoginRequest;
import com.workshop.vehicle_service.auth.dto.LoginResponse;
import com.workshop.vehicle_service.auth.dto.UtilisateurResponse;
import com.workshop.vehicle_service.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UtilisateurResponse> getCurrentUser() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }
}
