package com.app.medical_support.diagnosticexecution.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.diagnosticexecution.dto.PathologyCreateReqDTO;
import com.app.medical_support.diagnosticexecution.dto.PathologyDTO;
import com.app.medical_support.diagnosticexecution.dto.PathologyExamSearchCondition;
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
@RequestMapping("/api/pathology")
@RequiredArgsConstructor
@Tag(
        name = "병리 검사",
        description = "병리 검사(실행) CRUD. 조회는 GET /api/pathology(목록·쿼리 검색)와 GET /api/pathology/{id}(단건)만 사용합니다."
)
public class PathologyController {

    private final DiagnosticExecutionService diagnosticExecutionService;

    @Operation(
            summary = "병리 검사 목록 조회·검색",
            description = "DB 전체를 읽은 뒤 메모리에서 필터합니다. "
                    + "날짜 구간: 채취일시(collectedAt)가 있으면 그 시각으로, 없으면 생성일시(createdAt)로 구간을 맞춥니다. "
                    + "둘 다 null이면 기간을 주었을 때만 제외됩니다. "
                    + "진행상태(progressStatus): WAITING, IN_PROGRESS, COMPLETED 등 저장값과 대소문자 무시 완전 일치. "
                    + "조직 상태(tissueStatus): PathologyDTO.tissueStatus 문자열 부분 일치. "
                    + "목록·검색은 GET /api/pathology 한 경로만 제공합니다."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<PathologyDTO>>> findList(
            @Parameter(description = "환자명 부분 일치", example = "김")
            @RequestParam(value = "patientName", required = false) String patientName,
            @Parameter(description = "진료과명 부분 일치", example = "병리")
            @RequestParam(value = "departmentName", required = false) String departmentName,
            @Parameter(description = "조직 상태(tissueStatus) 부분 일치. 예: 고정완료, 검사중 등 저장 형태에 맞게 입력", example = "고정")
            @RequestParam(value = "tissueStatus", required = false) String tissueStatus,
            @Parameter(description = "검사 진행상태 완전 일치", example = "COMPLETED")
            @RequestParam(value = "progressStatus", required = false) String progressStatus,
            @Parameter(description = "기간 시작일(포함) yyyy-MM-dd. 비교 시각: collectedAt 우선, 없으면 createdAt", example = "2026-04-12")
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "기간 종료일(포함) yyyy-MM-dd. 비교 시각: collectedAt 우선, 없으면 createdAt", example = "2026-04-14")
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        PathologyExamSearchCondition condition = new PathologyExamSearchCondition();
        condition.setPatientName(patientName);
        condition.setDepartmentName(departmentName);
        condition.setTissueStatus(tissueStatus);
        condition.setProgressStatus(progressStatus);
        condition.setStartDate(startDate);
        condition.setEndDate(endDate);
        return ResponseEntity.ok(new ApiResponse<>(true, "Pathology list loaded.", diagnosticExecutionService.findPathologyList(condition)));
    }

    @Operation(
            summary = "병리 검사 단건 조회",
            description = "pathologyExamId(병리 검사 ID)로 1건을 조회합니다."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PathologyDTO>> findDetail(
            @Parameter(description = "병리 검사 ID (pathologyExamId)", required = true, example = "PTH202504010001")
            @PathVariable String id
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Pathology detail loaded.", diagnosticExecutionService.findPathologyDetail(id)));
    }

    @Operation(
            summary = "병리 검사 등록",
            description = "요청 본문: PathologyCreateReqDTO. 서버에서 pathologyExamId 발급 후 저장합니다."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<PathologyDTO>> register(@RequestBody PathologyCreateReqDTO pathologyDTO) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Pathology created.", diagnosticExecutionService.registerPathology(pathologyDTO)));
    }

    @Operation(
            summary = "병리 검사 수정",
            description = "본문에 pathologyExamId를 넣지 마세요(검증 오류). 경로 id 기준으로 갱신합니다."
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PathologyDTO>> modify(
            @Parameter(description = "병리 검사 ID", required = true)
            @PathVariable String id,
            @RequestBody PathologyDTO pathologyDTO
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Pathology updated.", diagnosticExecutionService.modifyPathology(id, pathologyDTO)));
    }

    @Operation(
            summary = "병리 검사 비활성화",
            description = "상태를 INACTIVE로 바꾸는 소프트 삭제에 가깝습니다."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> remove(
            @Parameter(description = "병리 검사 ID", required = true)
            @PathVariable String id
    ) {
        diagnosticExecutionService.deletePathology(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Pathology deactivated.", id));
    }
}
