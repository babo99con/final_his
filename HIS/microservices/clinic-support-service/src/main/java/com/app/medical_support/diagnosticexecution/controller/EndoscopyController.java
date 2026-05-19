package com.app.medical_support.diagnosticexecution.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.diagnosticexecution.dto.EndoscopyCreateReqDTO;
import com.app.medical_support.diagnosticexecution.dto.EndoscopyDTO;
import com.app.medical_support.diagnosticexecution.dto.EndoscopyExamSearchCondition;
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
@RequestMapping("/api/endoscopy")
@RequiredArgsConstructor
@Tag(
        name = "내시경 검사",
        description = "내시경 검사(실행) CRUD. 조회는 GET /api/endoscopy(목록·쿼리 검색)와 GET /api/endoscopy/{id}(단건)만 사용합니다."
)
public class EndoscopyController {

    private final DiagnosticExecutionService diagnosticExecutionService;

    @Operation(
            summary = "내시경 검사 목록 조회·검색",
            description = "DB 전체를 읽은 뒤 메모리에서 필터합니다. "
                    + "날짜 구간: 시술일시(procedureAt)가 있으면 그 시각으로, 없으면 생성일시(createdAt)로 구간을 맞춥니다. "
                    + "둘 다 null이면 기간을 주었을 때만 제외됩니다. "
                    + "진정 여부(sedationYn): Y, YES, TRUE 는 Y로, 그 외는 N으로 맞춘 뒤 비교합니다. "
                    + "진행상태(progressStatus): WAITING, IN_PROGRESS, COMPLETED 등 완전 일치(대소문자 무시). "
                    + "목록·검색은 GET /api/endoscopy 한 경로만 제공합니다."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<EndoscopyDTO>>> findList(
            @Parameter(description = "환자명 부분 일치", example = "이")
            @RequestParam(value = "patientName", required = false) String patientName,
            @Parameter(description = "진료과명 부분 일치", example = "소화기")
            @RequestParam(value = "departmentName", required = false) String departmentName,
            @Parameter(description = "진정 여부. 예: Y, N, yes", example = "Y")
            @RequestParam(value = "sedationYn", required = false) String sedationYn,
            @Parameter(description = "검사 진행상태 완전 일치", example = "IN_PROGRESS")
            @RequestParam(value = "progressStatus", required = false) String progressStatus,
            @Parameter(description = "기간 시작일(포함) yyyy-MM-dd. 비교 시각: procedureAt 우선, 없으면 createdAt", example = "2026-04-01")
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "기간 종료일(포함) yyyy-MM-dd. 비교 시각: procedureAt 우선, 없으면 createdAt", example = "2026-04-30")
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        EndoscopyExamSearchCondition condition = new EndoscopyExamSearchCondition();
        condition.setPatientName(patientName);
        condition.setDepartmentName(departmentName);
        condition.setSedationYn(sedationYn);
        condition.setProgressStatus(progressStatus);
        condition.setStartDate(startDate);
        condition.setEndDate(endDate);
        return ResponseEntity.ok(new ApiResponse<>(true, "Endoscopy list loaded.", diagnosticExecutionService.findEndoscopyList(condition)));
    }

    @Operation(
            summary = "내시경 검사 단건 조회",
            description = "endoscopyExamId(내시경 검사 ID)로 1건을 조회합니다."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EndoscopyDTO>> findDetail(
            @Parameter(description = "내시경 검사 ID (endoscopyExamId)", required = true, example = "END202504010001")
            @PathVariable String id
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Endoscopy detail loaded.", diagnosticExecutionService.findEndoscopyDetail(id)));
    }

    @Operation(
            summary = "내시경 검사 등록",
            description = "요청 본문: EndoscopyCreateReqDTO. 서버에서 endoscopyExamId 발급 후 저장합니다."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<EndoscopyDTO>> register(@RequestBody EndoscopyCreateReqDTO endoscopyDTO) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Endoscopy created.", diagnosticExecutionService.registerEndoscopy(endoscopyDTO)));
    }

    @Operation(
            summary = "내시경 검사 수정",
            description = "본문에 endoscopyExamId를 넣지 마세요(검증 오류). 경로 id 기준으로 갱신합니다."
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EndoscopyDTO>> modify(
            @Parameter(description = "내시경 검사 ID", required = true)
            @PathVariable String id,
            @RequestBody EndoscopyDTO endoscopyDTO
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Endoscopy updated.", diagnosticExecutionService.modifyEndoscopy(id, endoscopyDTO)));
    }

    @Operation(
            summary = "내시경 검사 비활성화",
            description = "상태를 INACTIVE로 바꾸는 소프트 삭제에 가깝습니다."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> remove(
            @Parameter(description = "내시경 검사 ID", required = true)
            @PathVariable String id
    ) {
        diagnosticExecutionService.deleteEndoscopy(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Endoscopy deactivated.", id));
    }
}
