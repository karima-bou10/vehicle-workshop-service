package com.workshop.vehicle_service.intervention.service.Imp;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}