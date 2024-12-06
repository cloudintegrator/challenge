package com.recargapay.entity.utils;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        ResponseEntity<Map<String, Object>> responseEntity = buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return responseEntity;

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleGenericRuntimeException(Exception ex) {
        ResponseEntity<Map<String, Object>> responseEntity = buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return responseEntity;

    }

    private ResponseEntity<Map<String, Object>> buildResponseEntity(HttpStatus status, String message) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("message", message);
        return new ResponseEntity<>(errorDetails, status);
    }
}

