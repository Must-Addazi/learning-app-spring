package com.mustapha.Spring_Students.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = "Une contrainte d'unicité a été violée.";
        String causeMessage = ex.getCause() != null ? ex.getCause().getMessage() : "";

        Map<String, String> constraintToMessage = new HashMap<>();
        constraintToMessage.put("UKcw70n30vtdbdhpvk92r3e4kt3", "Le CIN que vous avez fourni existe déjà.");
        constraintToMessage.put("UKfe0i52si7ybu0wjedj6motiim", "L'email que vous avez fourni existe déjà.");

        for (Map.Entry<String, String> entry : constraintToMessage.entrySet()) {
            if (causeMessage.contains(entry.getKey())) {
                message = entry.getValue();
                break;
            }
        }

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        System.out.println(errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
}

