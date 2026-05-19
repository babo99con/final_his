package com.app.medical_support.diagnosticresult.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.diagnosticresult.dto.ImagingResultCreateReqDTO;
import com.app.medical_support.diagnosticresult.dto.ImagingResultDTO;
import com.app.medical_support.diagnosticresult.dto.ImagingResultUpdateReqDTO;
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
@RequestMapping("/api/imagingResult")
@RequiredArgsConstructor
@Tag(name = "ImagingResult", description = "Imaging result API")
public class ImagingResultController {

    private final DiagnosticResultService diagnosticResultService;

    @Operation(summary = "영상 결과 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ImagingResultDTO>>> findList() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Imaging result list loaded.", diagnosticResultService.findImagingResultList()));
    }

    @Operation(summary = "영상 결과 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ImagingResultDTO>> findDetail(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Imaging result detail loaded.", diagnosticResultService.findImagingResultDetail(id)));
    }

    @Operation(summary = "영상 결과 등록")
    @PostMapping
    public ResponseEntity<ApiResponse<ImagingResultDTO>> register(@RequestBody ImagingResultCreateReqDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Imaging result created.", diagnosticResultService.registerImagingResult(dto)));
    }

    @Operation(summary = "영상 결과 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ImagingResultDTO>> modify(@PathVariable String id, @RequestBody ImagingResultUpdateReqDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Imaging result updated.", diagnosticResultService.modifyImagingResult(id, dto)));
    }

    @Operation(summary = "영상 결과 비활성화")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> remove(@PathVariable String id) {
        diagnosticResultService.deleteImagingResult(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Imaging result deactivated.", id));
    }
}
