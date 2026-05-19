package com.app.medical_support.diagnosticexecution.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.diagnosticexecution.dto.SpecimenCreateReqDTO;
import com.app.medical_support.diagnosticexecution.dto.SpecimenDTO;
import com.app.medical_support.diagnosticexecution.dto.SpecimenExamSearchCondition;
import com.app.medical_support.diagnosticexecution.service.DiagnosticExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/specimen")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "검체 검사",
        description = "검체 검사 CRUD. 조회는 (1) 복합 조건 목록 GET /api/specimen (2) 단일 필드 검색 GET /api/specimen/search 두 가지입니다."
)
public class SpecimenController {

    private final DiagnosticExecutionService specimenService;

    @Operation(
            summary = "검체 검사 목록 조회·검색 (복합 조건)",
            description = "한 번에 여러 조건(환자명·검체종류·상태·진행상태·날짜 범위 등)으로 목록을 필터합니다. "
                    + "날짜 구간: 채취일시(collectedAt) 우선, 없으면 생성일시(createdAt). "
                    + "단일 필드 검색은 별도 API GET /api/specimen/search 를 사용합니다."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<SpecimenDTO>>> findList(
            @Parameter(description = "환자명, 부분 일치")
            @RequestParam(value = "patientName", required = false) String patientName,
            @Parameter(description = "검체 종류, 부분 일치")
            @RequestParam(value = "specimenType", required = false) String specimenType,
            @Parameter(description = "검체 상태 코드, 대소문자 무시 완전 일치")
            @RequestParam(value = "specimenStatus", required = false) String specimenStatus,
            @Parameter(description = "진행상태 (WAITING / IN_PROGRESS / COMPLETED 등, 대소문자 무시 완전 일치)")
            @RequestParam(value = "progressStatus", required = false) String progressStatus,
            @Parameter(description = "기간 시작일(포함) yyyy-MM-dd. collectedAt 우선, 없으면 createdAt")
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "기간 종료일(포함) yyyy-MM-dd. collectedAt 우선, 없으면 createdAt")
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        SpecimenExamSearchCondition condition = new SpecimenExamSearchCondition();
        condition.setPatientName(patientName);
        condition.setSpecimenType(specimenType);
        condition.setSpecimenStatus(specimenStatus);
        condition.setProgressStatus(progressStatus);
        condition.setStartDate(startDate);
        condition.setEndDate(endDate);
        return ResponseEntity.ok(new ApiResponse<>(true, "Specimen list loaded.", specimenService.findSpecimenList(condition)));
    }

    @Operation(
            summary = "검체 검사 단일 필드 검색",
            description = "searchType 하나와 searchValue 하나만으로 Repository 검색합니다. "
                    + "복합 조건(날짜 범위·여러 필드 동시)은 위의 GET /api/specimen 목록 API를 사용하세요."
    )
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SpecimenDTO>>> searchSpecimens(
            @Parameter(description = "검색 구분: testExecutionId | specimenType | specimenStatus")
            @RequestParam("searchType") String searchType,
            @Parameter(description = "검색어 (searchType에 맞는 값)")
            @RequestParam("searchValue") String searchValue
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Specimen search completed.", specimenService.searchSpecimen(searchType, searchValue)));
    }

    @Operation(summary = "검체 검사 단건 조회", description = "검체 검사 1건을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SpecimenDTO>> findSpecimenDetail(
            @Parameter(description = "Specimen exam ID")
            @PathVariable String id
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Specimen detail loaded.", specimenService.findSpecimenDetail(id)));
    }

    @Operation(summary = "검체 검사 등록", description = "새 검체 검사를 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<SpecimenDTO>> registerSpecimen(
            @Parameter(description = "Specimen exam request body")
            @RequestBody SpecimenCreateReqDTO specimen
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Specimen created.", specimenService.registerSpecimen(specimen)));
    }

    @Operation(summary = "검체 검사 수정", description = "검체 검사 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SpecimenDTO>> modifySpecimen(
            @Parameter(description = "Specimen exam ID")
            @PathVariable String id,
            @Parameter(description = "Specimen exam request body")
            @RequestBody SpecimenDTO specimenDTO
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Specimen updated.", specimenService.modifySpecimen(id, specimenDTO)));
    }

    @Operation(summary = "검체 검사 비활성화", description = "상태값을 INACTIVE로 변경합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> removeSpecimen(
            @Parameter(description = "Specimen exam ID")
            @PathVariable String id
    ) {
        specimenService.deleteSpecimen(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Specimen deactivated.", id));
    }
}
