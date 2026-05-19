package com.app.medical_support.diagnosticresult.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.diagnosticresult.dto.SpecimenTestResultCreateReqDTO;
import com.app.medical_support.diagnosticresult.dto.SpecimenTestResultDTO;
import com.app.medical_support.diagnosticresult.dto.SpecimenTestResultUpdateReqDTO;
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
@RequestMapping("/api/specimenResult")
@RequiredArgsConstructor
@Tag(name = "SpecimenResult", description = "Specimen result API")
public class SpecimenResultController {

    private final DiagnosticResultService diagnosticResultService;

    @Operation(summary = "검체 결과 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<SpecimenTestResultDTO>>> findList() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Specimen result list loaded.", diagnosticResultService.findSpecimenResultList()));
    }

    @Operation(summary = "검체 결과 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SpecimenTestResultDTO>> findDetail(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Specimen result detail loaded.", diagnosticResultService.findSpecimenResultDetail(id)));
    }

    @Operation(summary = "검체 결과 등록")
    @PostMapping
    public ResponseEntity<ApiResponse<SpecimenTestResultDTO>> register(@RequestBody SpecimenTestResultCreateReqDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Specimen result created.", diagnosticResultService.registerSpecimenResult(dto)));
    }

    @Operation(summary = "검체 결과 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SpecimenTestResultDTO>> modify(@PathVariable String id, @RequestBody SpecimenTestResultUpdateReqDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Specimen result updated.", diagnosticResultService.modifySpecimenResult(id, dto)));
    }

    @Operation(summary = "검체 결과 비활성화")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> remove(@PathVariable String id) {
        diagnosticResultService.deleteSpecimenResult(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Specimen result deactivated.", id));
    }
}
