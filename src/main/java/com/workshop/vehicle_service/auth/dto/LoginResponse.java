package com.workshop.vehicle_service.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    String token;
    String type;
    Date expiresIn;
}
