package com.app.medical_support.diagnosticexecution.exception.handler;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.diagnosticexecution.exception.DiagnosticExecutionNotFoundException;
import com.app.medical_support.diagnosticexecution.exception.EndoscopyNotFoundException;
import com.app.medical_support.diagnosticexecution.exception.ImagingNotFoundException;
import com.app.medical_support.diagnosticexecution.exception.PathologyNotFoundException;
import com.app.medical_support.diagnosticexecution.exception.PhysiologicalNotFoundException;
import com.app.medical_support.diagnosticexecution.exception.SpecimenNotFoundException;
import com.app.medical_support.diagnosticexecution.exception.TestExecutionNotFoundExecution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class DiagnosticExecutionExceptionHandler {

    @ExceptionHandler(DiagnosticExecutionNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleDiagnosticExecutionNotFound(DiagnosticExecutionNotFoundException ex) {
        log.warn("DiagnosticExecutionNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(SpecimenNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleSpecimenNotFound(SpecimenNotFoundException ex) {
        log.warn("SpecimenNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(TestExecutionNotFoundExecution.class)
    public ResponseEntity<ApiResponse<Void>> handleTestExecutionNotFound(TestExecutionNotFoundExecution ex) {
        log.warn("TestExecutionNotFoundExecution: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }


    @ExceptionHandler(ImagingNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleImagingNotFound(ImagingNotFoundException ex) {
        log.warn("ImagingNotFoundExeption: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(EndoscopyNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEndoscopyNotFound(EndoscopyNotFoundException ex) {
        log.warn("EndoscopyNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(PathologyNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePathologyNotFound(PathologyNotFoundException ex) {
        log.warn("PathologyNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(PhysiologicalNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePhysiologicalNotFound(PhysiologicalNotFoundException ex) {
        log.warn("PhysiologicalNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }
}
