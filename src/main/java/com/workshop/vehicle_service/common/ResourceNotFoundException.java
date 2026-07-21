package com.workshop.vehicle_service.common;
public class ResourceNotFoundException extends  RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }

}
