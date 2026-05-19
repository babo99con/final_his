package com.app.medical_support.nursingtreatment.exception.handler;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.nursingtreatment.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RecordExceptionHandler {

    @ExceptionHandler(RecordReceptionValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleRecordReceptionValidation(RecordReceptionValidationException ex) {
        log.warn("RecordReceptionValidationException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(RecordReceptionLookupException.class)
    public ResponseEntity<ApiResponse<Void>> handleRecordReceptionLookup(RecordReceptionLookupException ex) {
        log.warn("RecordReceptionLookupException: {}", ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleRecordNotFound(RecordNotFoundException ex) {
        log.warn("RecordNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(MedicationRecordNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleMedicationRecordNotFound(MedicationRecordNotFoundException ex) {
        log.warn("MedicationRecordNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(TreatmentResultNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTreatmentResultNotFound(TreatmentResultNotFoundException ex) {
        log.warn("TreatmentResultNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(RecordSearchValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleRecordSearchValidation(RecordSearchValidationException ex) {
        log.warn("RecordSearchValidationException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

}
