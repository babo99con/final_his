package kr.co.hospital.patients.patient.exception;

import com.hms.util.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class FlagExceptionHandler {

    @ExceptionHandler(FlagNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleFlagNotFound(FlagNotFoundException ex) {
        log.warn("FlagNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }
}