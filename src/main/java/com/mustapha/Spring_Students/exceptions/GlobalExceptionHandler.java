package com.mustapha.Spring_Students.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = "A uniqueness constraint has been violated.";
        String causeMessage = ex.getCause() != null ? ex.getCause().getMessage() : "";

        Map<String, String> constraintToMessage = new HashMap<>();
        constraintToMessage.put("UKcw70n30vtdbdhpvk92r3e4kt3", "The CIN you provided already exists.");
        constraintToMessage.put("UKfe0i52si7ybu0wjedj6motiim", "The Email you provided already exists.");

        for (Map.Entry<String, String> entry : constraintToMessage.entrySet()) {
            if (causeMessage.contains(entry.getKey())) {
                message = entry.getValue();
                break;
            }
        }

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        log.error(message);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //  400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
    // 401 Unauthorized
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleUnauthorizedException(AuthenticationCredentialsNotFoundException ex){
        log.warn("Unauthorized access: {}", ex.getMessage(), ex);
        return buildResponse("You must be authenticated to access this resource.", HttpStatus.UNAUTHORIZED);
    }
    // 403 Forbidden
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleForbiddenException(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage(), ex);
        return buildResponse("You do not have permission to access this resource.", HttpStatus.FORBIDDEN);
    }

    // 🔥 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {
        log.error("Unhandled Exception [{}]: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return buildResponse("An internal error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String,String>> handleAuthException(BadCredentialsException ex){
        log.warn("Unauthorized access: {}", ex.getMessage(), ex);
        return buildResponse("Bad Credentials.", HttpStatus.UNAUTHORIZED);
    }
    private ResponseEntity<Map<String, String>> buildResponse(String message, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        return new ResponseEntity<>(errorResponse, status);
    }
}

