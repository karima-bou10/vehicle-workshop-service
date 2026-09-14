package com.workshop.vehicle_service.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "Ressource introuvable", ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex){
        if (ex.getMessage() != null && ex.getMessage().contains("interventions existantes")) {
            return buildResponse(HttpStatus.CONFLICT, "Conflit d'intégrité", ex.getMessage());
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "Requete invalide", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err ->err.getField() + ":" +err.getDefaultMessage())
                .collect(Collectors.joining(" ; "));
        return buildResponse(HttpStatus.BAD_REQUEST, "Données invalides", message);
    }

    private  ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String erreur, String message){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                status.value(),
                erreur,
                message
        );
        return ResponseEntity.status(status).body(errorResponse);

    }


}
