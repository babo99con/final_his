package com.staff.common.exception;

import com.hms.util.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ApiResponse<Void>> handleUpload(FileUploadException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, e.getMessage(), null));
    }
}
