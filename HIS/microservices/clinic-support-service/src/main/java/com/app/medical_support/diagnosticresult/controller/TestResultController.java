package com.app.medical_support.diagnosticresult.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.diagnosticresult.dto.TestResultDetailDTO;
import com.app.medical_support.diagnosticresult.dto.TestResultListDTO;
import com.app.medical_support.diagnosticresult.dto.TestResultProgressStatusUpdateReqDTO;
import com.app.medical_support.diagnosticresult.dto.TestResultSearchCondition;
import com.app.medical_support.diagnosticresult.dto.TestResultUpdateReqDTO;
import com.app.medical_support.diagnosticresult.service.DiagnosticResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/testResult")
@RequiredArgsConstructor
@Tag(
        name = "통합 검사 결과",
        description = "영상·검체·병리·내시경·생리기능 검사 결과를 한 API로 목록·상세·수정합니다. "
                + "목록·검색은 GET /api/testResult 한 경로입니다."
)
public class TestResultController {

    private final DiagnosticResultService diagnosticResultService;

    @Operation(
            summary = "통합 검사 결과 목록·검색",
            description = "5종 검사 결과를 한 리스트로 합친 뒤 서버에서 필터링합니다. "
                    + "날짜: confirmedAt(확정일시)이 있는 행만 기간 필터에 포함되며, null이면 기간 조건 시 제외됩니다. "
                    + "미입력 파라미터는 조건에서 제외됩니다."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<TestResultListDTO>>> findList(
            @Parameter(description = "검사 유형", example = "IMAGING")
            @RequestParam(value = "resultType", required = false) String resultType,
            @Parameter(description = "결과 PK (유형별 결과 ID, 예: imagingResultId)", example = "IMGRES202504010001")
            @RequestParam(value = "resultId", required = false) String resultId,
            @Parameter(description = "환자명 부분 일치", example = "홍길")
            @RequestParam(value = "patientName", required = false) String patientName,
            @Parameter(description = "상세 코드(detailCode) 부분 일치")
            @RequestParam(value = "detailCode", required = false) String detailCode,
            @Parameter(description = "진료과명 부분 일치", example = "내과")
            @RequestParam(value = "departmentName", required = false) String departmentName,
            @Parameter(description = "결과 상태", example = "ACTIVE")
            @RequestParam(value = "status", required = false) String status,
            @Parameter(description = "결과일시(확정) 시작일 포함, yyyy-MM-dd", example = "2025-04-01")
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "결과일시(확정) 종료일 포함, yyyy-MM-dd", example = "2025-04-30")
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "true: 비활성(INACTIVE) 결과 포함 / false·미입력: 비활성 제외(기본)", example = "false")
            @RequestParam(value = "includeInactive", required = false) Boolean includeInactive
    ) {
        TestResultSearchCondition condition = new TestResultSearchCondition();
        condition.setResultType(resultType);
        condition.setResultId(resultId);
        condition.setPatientName(patientName);
        condition.setDetailCode(detailCode);
        condition.setDepartmentName(departmentName);
        condition.setStatus(status);
        condition.setStartDate(startDate);
        condition.setEndDate(endDate);
        condition.setIncludeInactive(includeInactive);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Integrated test result list loaded.",
                diagnosticResultService.findTestResultList(condition)
        ));
    }

    @Operation(
            summary = "통합 검사 결과 상세",
            description = "`resultType` + `resultId` 로 5종 중 해당 결과 1건을 조회합니다. 유형·ID 조합은 목록 응답의 resultType, resultId 와 동일해야 합니다."
    )
    @GetMapping("/{resultType}/{resultId}")
    public ResponseEntity<ApiResponse<TestResultDetailDTO>> findDetail(
            @Parameter(description = "검사 유형", example = "PATHOLOGY")
            @PathVariable String resultType,
            @Parameter(description = "결과 PK", example = "PTHRES202504010001")
            @PathVariable String resultId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Integrated test result detail loaded.",
                diagnosticResultService.findTestResultDetail(resultType, resultId)
        ));
    }

    @Operation(
            summary = "통합 검사 결과 수정",
            description = "경로의 유형·ID에 맞는 결과만 갱신합니다. 요청 본문 스키마는 `TestResultUpdateReqDTO` 를 참고하세요."
    )
    @PutMapping("/{resultType}/{resultId}")
    public ResponseEntity<ApiResponse<TestResultDetailDTO>> modify(
            @Parameter(description = "검사 유형", example = "ENDOSCOPY")
            @PathVariable String resultType,
            @Parameter(description = "결과 PK")
            @PathVariable String resultId,
            @RequestBody TestResultUpdateReqDTO dto
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Integrated test result updated.",
                diagnosticResultService.modifyTestResult(resultType, resultId, dto)
        ));
    }

    @Operation(
            summary = "통합 검사 결과 진행상태 변경",
            description = "경로의 resultId로 결과 타입을 자동 판별해 progressStatus를 변경합니다. "
                    + "허용값은 IN_PROGRESS, COMPLETED이며 COMPLETED 이후 재변경은 불가합니다."
    )
    @PatchMapping("/{resultId}/status")
    public ResponseEntity<ApiResponse<TestResultDetailDTO>> updateProgressStatus(
            @Parameter(description = "결과 PK")
            @PathVariable String resultId,
            @RequestBody TestResultProgressStatusUpdateReqDTO dto
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Integrated test result progress status updated.",
                diagnosticResultService.updateTestResultProgressStatus(resultId, dto)
        ));
    }
}
