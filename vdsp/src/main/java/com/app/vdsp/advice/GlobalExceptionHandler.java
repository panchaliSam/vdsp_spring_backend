package com.app.vdsp.advice;

import com.app.vdsp.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Handle ResponseStatusExceptions
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDto> handleBundleRefillException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(new ErrorDto(exception.getStatusCode().value(), exception.getReason()));
    }

    // Handle NullPointerException
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDto> handleNullPointerException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto(500, "Null value encountered!"));
    }
}
