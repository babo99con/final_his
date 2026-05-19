package com.app.medical_support.diagnosticresult.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.diagnosticresult.dto.PhysiologicalResultCreateReqDTO;
import com.app.medical_support.diagnosticresult.dto.PhysiologicalResultDTO;
import com.app.medical_support.diagnosticresult.dto.PhysiologicalResultUpdateReqDTO;
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
@RequestMapping("/api/physiologicalResult")
@RequiredArgsConstructor
@Tag(name = "PhysiologicalResult", description = "Physiological result API")
public class PhysiologicalResultController {

    private final DiagnosticResultService diagnosticResultService;

    @Operation(summary = "생리 기능 검사 결과 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PhysiologicalResultDTO>>> findList() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Physiological result list loaded.", diagnosticResultService.findPhysiologicalResultList()));
    }

    @Operation(summary = "생리 기능 검사 결과 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PhysiologicalResultDTO>> findDetail(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Physiological result detail loaded.", diagnosticResultService.findPhysiologicalResultDetail(id)));
    }

    @Operation(summary = "생리 기능 검사 결과 등록")
    @PostMapping
    public ResponseEntity<ApiResponse<PhysiologicalResultDTO>> register(@RequestBody PhysiologicalResultCreateReqDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Physiological result created.", diagnosticResultService.registerPhysiologicalResult(dto)));
    }

    @Operation(summary = "생리 기능 검사 결과 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PhysiologicalResultDTO>> modify(@PathVariable String id, @RequestBody PhysiologicalResultUpdateReqDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Physiological result updated.", diagnosticResultService.modifyPhysiologicalResult(id, dto)));
    }

    @Operation(summary = "생리 기능 검사 결과 비활성화")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> remove(@PathVariable String id) {
        diagnosticResultService.deletePhysiologicalResult(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Physiological result deactivated.", id));
    }
}
