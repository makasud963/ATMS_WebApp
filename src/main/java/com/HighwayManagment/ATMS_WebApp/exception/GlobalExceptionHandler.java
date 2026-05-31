package com.HighwayManagment.ATMS_WebApp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(
            RuntimeException ex
    ) {

        Map<String, Object> response = new HashMap<>();

        response.put("success", false);
        response.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(
            Exception ex
    ) {

        Map<String, Object> response = new HashMap<>();

        response.put("success", false);
        response.put("message", "Internal Server Error");

        return ResponseEntity.internalServerError()
                .body(response);
    }
}