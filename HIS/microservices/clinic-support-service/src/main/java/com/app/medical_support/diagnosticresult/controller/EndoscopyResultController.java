package com.app.medical_support.diagnosticresult.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.diagnosticresult.dto.EndoscopyResultCreateReqDTO;
import com.app.medical_support.diagnosticresult.dto.EndoscopyResultDTO;
import com.app.medical_support.diagnosticresult.dto.EndoscopyResultUpdateReqDTO;
import com.app.medical_support.diagnosticresult.service.DiagnosticResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/endoscopyResult")
@RequiredArgsConstructor
@Tag(name = "EndoscopyResult", description = "Endoscopy result API")
public class EndoscopyResultController {

    private final DiagnosticResultService diagnosticResultService;

    @Operation(summary = "내시경 결과 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<EndoscopyResultDTO>>> findList() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Endoscopy result list loaded.", diagnosticResultService.findEndoscopyResultList()));
    }

    @Operation(summary = "내시경 결과 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EndoscopyResultDTO>> findDetail(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Endoscopy result detail loaded.", diagnosticResultService.findEndoscopyResultDetail(id)));
    }

    @Operation(summary = "내시경 결과 등록")
    @PostMapping
    public ResponseEntity<ApiResponse<EndoscopyResultDTO>> register(@RequestBody EndoscopyResultCreateReqDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Endoscopy result created.", diagnosticResultService.registerEndoscopyResult(dto)));
    }

    @Operation(summary = "내시경 결과 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EndoscopyResultDTO>> modify(@PathVariable String id, @RequestBody EndoscopyResultUpdateReqDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Endoscopy result updated.", diagnosticResultService.modifyEndoscopyResult(id, dto)));
    }

    @Operation(summary = "내시경 결과 비활성화")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> remove(@PathVariable String id) {
        diagnosticResultService.deleteEndoscopyResult(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Endoscopy result deactivated.", id));
    }
}
