package com.app.medical_support.common.exception.handler;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.common.exception.InvalidRequestException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidRequest(InvalidRequestException ex) {
        log.warn("InvalidRequestException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getMostSpecificCause();
        String message = "Invalid request body.";

        if (cause instanceof UnrecognizedPropertyException unrecognizedPropertyException) {
            message = "Unknown request field: " + unrecognizedPropertyException.getPropertyName();
        }

        log.warn("HttpMessageNotReadableException: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, message, null));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus status = ex.getStatus();
        String message = ex.getReason() != null ? ex.getReason() : status.getReasonPhrase();
        log.warn("ResponseStatusException: status={}, message={}", status.value(), message);
        return ResponseEntity.status(status)
                .body(new ApiResponse<>(false, message, null));
    }
}
