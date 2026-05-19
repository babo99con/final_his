package com.app.medical_support.diagnosticexecution.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.diagnosticexecution.dto.ImagingCreateReqDTO;
import com.app.medical_support.diagnosticexecution.dto.ImagingDTO;
import com.app.medical_support.diagnosticexecution.dto.ImagingExamSearchCondition;
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
@RequestMapping("/api/imaging")
@RequiredArgsConstructor
@Tag(name = "Imaging", description = "Imaging exam API")
public class ImagingController {

    private final DiagnosticExecutionService diagnosticExecutionService;

    @Operation(
            summary = "영상 검사 목록 조회·검색",
            description = "GET /api/imaging 만 사용합니다. 쿼리스트링으로 필터하며, 비운 항목은 조건에서 제외됩니다. "
                    + "생성일시(createdAt) 기준으로 startDate~endDate 구간을 필터합니다."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<ImagingDTO>>> findList(
            @Parameter(description = "환자명, 부분 일치")
            @RequestParam(value = "patientName", required = false) String patientName,
            @Parameter(description = "진료과명, 부분 일치")
            @RequestParam(value = "departmentName", required = false) String departmentName,
            @Parameter(description = "진행상태 (WAITING / IN_PROGRESS / COMPLETED 등, 대소문자 무시 완전 일치)")
            @RequestParam(value = "progressStatus", required = false) String progressStatus,
            @Parameter(description = "생성일시 필터 시작일 (포함), 형식 yyyy-MM-dd")
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "생성일시 필터 종료일 (포함), 형식 yyyy-MM-dd")
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "검사명: detailCode 또는 imagingType에 대한 부분 일치")
            @RequestParam(value = "examName", required = false) String examName
    ) {
        ImagingExamSearchCondition condition = new ImagingExamSearchCondition();
        condition.setPatientName(patientName);
        condition.setDepartmentName(departmentName);
        condition.setProgressStatus(progressStatus);
        condition.setStartDate(startDate);
        condition.setEndDate(endDate);
        condition.setExamName(examName);
        return ResponseEntity.ok(new ApiResponse<>(true, "Imaging list loaded.", diagnosticExecutionService.findImagingList(condition)));
    }

    @Operation(summary = "영상 검사 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ImagingDTO>> findDetail(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Imaging detail loaded.", diagnosticExecutionService.findImagingDetail(id)));
    }

    @Operation(summary = "영상 검사 등록")
    @PostMapping
    public ResponseEntity<ApiResponse<ImagingDTO>> register(@RequestBody ImagingCreateReqDTO imagingDTO) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Imaging created.", diagnosticExecutionService.registerImaging(imagingDTO)));
    }

    @Operation(summary = "영상 검사 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ImagingDTO>> modify(@PathVariable String id, @RequestBody ImagingDTO imagingDTO) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Imaging updated.", diagnosticExecutionService.modifyImaging(id, imagingDTO)));
    }

    @Operation(summary = "영상 검사 비활성화")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> remove(@PathVariable String id) {
        diagnosticExecutionService.deleteImaging(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Imaging deactivated.", id));
    }
}
