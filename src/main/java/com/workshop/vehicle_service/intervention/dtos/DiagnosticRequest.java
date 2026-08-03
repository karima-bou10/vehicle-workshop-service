package com.workshop.vehicle_service.intervention.dtos;

import jakarta.validation.constraints.NotNull;

public record DiagnosticRequest(@NotNull
                                String diagnostic) {
}
