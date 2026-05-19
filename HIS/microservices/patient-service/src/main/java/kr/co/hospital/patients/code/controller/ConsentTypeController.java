package kr.co.hospital.patients.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.hospital.patients.code.dto.ConsentTypeReq;
import kr.co.hospital.patients.code.dto.ConsentTypeRes;
import kr.co.hospital.patients.code.service.ConsentTypeService;
import com.hms.util.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consent-types")
@RequiredArgsConstructor
@Tag(name = "Consent Type", description = "Consent type management")
public class ConsentTypeController {

    private final ConsentTypeService consentTypeService;

    @Operation(summary = "List active consent types")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ConsentTypeRes>>> listActive() {
        List<ConsentTypeRes> list = consentTypeService.findActive();
        return ResponseEntity.ok(new ApiResponse<>(true, "OK", list));
    }

    @Operation(summary = "List all consent types")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ConsentTypeRes>>> listAll() {
        List<ConsentTypeRes> list = consentTypeService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "OK", list));
    }

    @Operation(summary = "Create consent type")
    @PostMapping
    public ResponseEntity<ApiResponse<ConsentTypeRes>> create(
            @RequestBody ConsentTypeReq req
    ) {
        ConsentTypeRes created = consentTypeService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Created", created));
    }

    @Operation(summary = "Update consent type")
    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<ConsentTypeRes>> update(
            @PathVariable String code,
            @RequestBody ConsentTypeReq req
    ) {
        ConsentTypeRes updated = consentTypeService.update(code, req);
        return ResponseEntity.ok(new ApiResponse<>(true, "OK", updated));
    }

    @Operation(summary = "Deactivate consent type")
    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable String code) {
        consentTypeService.deactivate(code);
        return ResponseEntity.ok(new ApiResponse<>(true, "OK", null));
    }
}
