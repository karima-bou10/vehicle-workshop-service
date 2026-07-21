package com.workshop.vehicle_service.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Username cannot be blank")
    public String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 4, max = 20, message = "Password must be between 6 and 20 characters")
    public String password;
}
