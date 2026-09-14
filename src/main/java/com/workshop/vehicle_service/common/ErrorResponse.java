package com.workshop.vehicle_service.common;

public record ErrorResponse(    String timestamp,
                                int status,
                                String erreur,
                                String message) { }
