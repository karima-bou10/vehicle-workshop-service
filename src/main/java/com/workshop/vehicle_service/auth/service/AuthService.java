package com.workshop.vehicle_service.auth.service;

import com.workshop.vehicle_service.auth.dto.LoginRequest;
import com.workshop.vehicle_service.auth.dto.LoginResponse;
import com.workshop.vehicle_service.auth.dto.UtilisateurResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    UtilisateurResponse getCurrentUser();
}
