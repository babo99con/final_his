package com.app.medical_support.diagnosticresult.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.diagnosticresult.dto.PathologyResultCreateReqDTO;
import com.app.medical_support.diagnosticresult.dto.PathologyResultDTO;
import com.app.medical_support.diagnosticresult.dto.PathologyResultUpdateReqDTO;
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
@RequestMapping("/api/pathologyResult")
@RequiredArgsConstructor
@Tag(name = "PathologyResult", description = "Pathology result API")
public class PathologyResultController {

    private final DiagnosticResultService diagnosticResultService;

    @Operation(summary = "병리 결과 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PathologyResultDTO>>> findList() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Pathology result list loaded.", diagnosticResultService.findPathologyResultList()));
    }

    @Operation(summary = "병리 결과 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PathologyResultDTO>> findDetail(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Pathology result detail loaded.", diagnosticResultService.findPathologyResultDetail(id)));
    }

    @Operation(summary = "병리 결과 등록")
    @PostMapping
    public ResponseEntity<ApiResponse<PathologyResultDTO>> register(@RequestBody PathologyResultCreateReqDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Pathology result created.", diagnosticResultService.registerPathologyResult(dto)));
    }

    @Operation(summary = "병리 결과 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PathologyResultDTO>> modify(@PathVariable String id, @RequestBody PathologyResultUpdateReqDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Pathology result updated.", diagnosticResultService.modifyPathologyResult(id, dto)));
    }

    @Operation(summary = "병리 결과 비활성화")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> remove(@PathVariable String id) {
        diagnosticResultService.deletePathologyResult(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Pathology result deactivated.", id));
    }
}
