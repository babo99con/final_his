package kr.co.hospital.patients.consent.exception;

import com.hms.util.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ConsentExceptionHandler {

    @ExceptionHandler(ConsentNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleConsentNotFound(ConsentNotFoundException ex) {
        log.warn("ConsentNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }
}