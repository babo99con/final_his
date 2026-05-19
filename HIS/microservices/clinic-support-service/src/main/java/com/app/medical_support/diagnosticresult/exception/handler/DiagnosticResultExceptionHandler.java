package com.app.medical_support.diagnosticresult.exception.handler;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.diagnosticresult.exception.DiagnosticResultNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class DiagnosticResultExceptionHandler {

    @ExceptionHandler(DiagnosticResultNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleDiagnosticResultNotFound(DiagnosticResultNotFoundException ex) {
        log.warn("DiagnosticResultNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }
}
