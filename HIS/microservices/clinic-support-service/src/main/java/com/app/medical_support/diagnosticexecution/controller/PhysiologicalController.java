package com.app.medical_support.diagnosticexecution.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.diagnosticexecution.dto.PhysiologicalCreateReqDTO;
import com.app.medical_support.diagnosticexecution.dto.PhysiologicalDTO;
import com.app.medical_support.diagnosticexecution.dto.PhysiologicalExamSearchCondition;
import com.app.medical_support.diagnosticexecution.service.DiagnosticExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/physiological")
@RequiredArgsConstructor
@Tag(name = "Physiological", description = "Physiological exam API")
public class PhysiologicalController {

    private final DiagnosticExecutionService diagnosticExecutionService;

    @Operation(
            summary = "생리 기능 검사 목록 조회·검색",
            description = "GET /api/physiological 만 사용합니다. 쿼리스트링으로 필터하며, 비운 항목은 조건에서 제외됩니다. "
                    + "생성일시(createdAt) 기준으로 startDate~endDate 구간을 필터합니다."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<PhysiologicalDTO>>> findList(
            @Parameter(description = "생리기능검사 ID, 대소문자 무시 완전 일치")
            @RequestParam(value = "physiologicalExamId", required = false) String physiologicalExamId,
            @Parameter(description = "환자명, 부분 일치")
            @RequestParam(value = "patientName", required = false) String patientName,
            @Parameter(description = "진료과명, 부분 일치")
            @RequestParam(value = "departmentName", required = false) String departmentName,
            @Parameter(description = "진행상태 (WAITING / IN_PROGRESS / COMPLETED 등, 대소문자 무시 완전 일치)")
            @RequestParam(value = "progressStatus", required = false) String progressStatus,
            @Parameter(description = "생성일시 필터 시작일 (포함), 형식 yyyy-MM-dd")
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "생성일시 필터 종료일 (포함), 형식 yyyy-MM-dd")
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        PhysiologicalExamSearchCondition condition = new PhysiologicalExamSearchCondition();
        condition.setPhysiologicalExamId(physiologicalExamId);
        condition.setPatientName(patientName);
        condition.setDepartmentName(departmentName);
        condition.setProgressStatus(progressStatus);
        condition.setStartDate(startDate);
        condition.setEndDate(endDate);
        return ResponseEntity.ok(new ApiResponse<>(true, "Physiological list loaded.", diagnosticExecutionService.findPhysiologicalList(condition)));
    }

    @Operation(summary = "생리 기능 검사 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PhysiologicalDTO>> findDetail(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Physiological detail loaded.", diagnosticExecutionService.findPhysiologicalDetail(id)));
    }

    @Operation(summary = "생리 기능 검사 등록")
    @PostMapping
    public ResponseEntity<ApiResponse<PhysiologicalDTO>> register(@RequestBody PhysiologicalCreateReqDTO physiologicalDTO) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Physiological created.", diagnosticExecutionService.registerPhysiological(physiologicalDTO)));
    }

    @Operation(summary = "생리 기능 검사 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PhysiologicalDTO>> modify(@PathVariable String id, @RequestBody PhysiologicalDTO physiologicalDTO) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Physiological updated.", diagnosticExecutionService.modifyPhysiological(id, physiologicalDTO)));
    }

    @Operation(summary = "생리 기능 검사 비활성화")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> remove(@PathVariable String id) {
        diagnosticExecutionService.deletePhysiological(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Physiological deactivated.", id));
    }
}
