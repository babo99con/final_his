package com.app.medical_support.common.integration.reception.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.common.integration.reception.dto.OutpatientReceptionDTO;
import com.app.medical_support.common.integration.reception.service.ReceptionIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/receptions")
@RequiredArgsConstructor
@Tag(name = "ReceptionIntegration", description = "Reception integration API")
public class ReceptionIntegrationController {

    private final ReceptionIntegrationService receptionIntegrationService;

    @Operation(summary = "접수 목록(조건) 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OutpatientReceptionDTO>>> findList(
            @Parameter(description = "진료일(yyyy-MM-dd)", required = true, example = "2026-04-15")
            @RequestParam String visitDate,
            @Parameter(description = "진료유형", example = "OUTPATIENT")
            @RequestParam(required = false, defaultValue = "OUTPATIENT") String visitType,
            @Parameter(description = "상태 목록(CSV)", example = "WAITING,CALLED,IN_PROGRESS")
            @RequestParam(required = false, defaultValue = "WAITING,CALLED,IN_PROGRESS") String statuses,
            @Parameter(description = "진료과 ID (접수 MSA와 동일, 예: DEPT-003)")
            @RequestParam(required = false) String departmentId,
            @Parameter(description = "의사 ID (접수 MSA와 동일)")
            @RequestParam(required = false) String doctorId
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Reception list loaded.",
                        receptionIntegrationService.findListByConditions(
                                visitDate,
                                visitType,
                                statuses,
                                departmentId,
                                doctorId
                        )
                )
        );
    }

    @Operation(summary = "외래 접수 대기열 조회 (접수 MSA queue 프록시, date 생략 시 오늘)")
    @GetMapping("/queue")
    public ResponseEntity<ApiResponse<List<OutpatientReceptionDTO>>> findQueue(
            @Parameter(description = "조회일 (yyyy-MM-dd), 생략 시 서버 오늘")
            @RequestParam(required = false) String date,
            @Parameter(description = "진료과 ID (예: DEPT-003)")
            @RequestParam(required = false) String departmentId,
            @Parameter(description = "의사 ID")
            @RequestParam(required = false) String doctorId
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Reception queue loaded.",
                        receptionIntegrationService.findQueue(date, departmentId, doctorId)
                )
        );
    }

    @Operation(summary = "접수 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OutpatientReceptionDTO>> findDetail(
            @Parameter(description = "Reception ID")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Reception detail loaded.",
                        receptionIntegrationService.findDetail(id)
                )
        );
    }
}
