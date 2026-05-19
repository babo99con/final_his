package com.app.medical_support.common.integration.clinical.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.common.integration.clinical.dto.ClinicalVitalAssessResponse;
import com.app.medical_support.common.integration.clinical.service.ClinicalIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
@Tag(
        name = "ClinicalIntegration",
        description = "진료 MSA 연동 — 활력·문진. 간호 기록 상세의 「의사 문진」 탭은 **접수(reception) 기준** 엔드포인트를 사용한다."
)
public class ClinicalIntegrationController {

    private static final String MSG_VITAL_OK = "활력·문진 조회 성공";
    private static final String MSG_VITAL_EMPTY_VISIT = "해당 방문에 등록된 활력·문진이 없습니다.";
    private static final String MSG_VITAL_EMPTY_RECEPTION = "해당 접수로 조회한 활력·문진이 없습니다. 진료 방문이 없거나 아직 등록되지 않았을 수 있습니다.";

    private final ClinicalIntegrationService clinicalIntegrationService;

    @Operation(summary = "visitId 기준 활력·문진 단건 조회 (진료와 동일 키)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "항상 200. 데이터 없으면 success=true, result=null",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "visitId가 유효하지 않음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "502", description = "진료 MSA 연결 실패·진료 서버 오류")
    })
    @GetMapping("/{visitId}/vital-assess")
    public ResponseEntity<ApiResponse<ClinicalVitalAssessResponse>> findVitalAssessByVisitId(
            @Parameter(description = "진료 방문 ID") @PathVariable Long visitId
    ) {
        ClinicalVitalAssessResponse body = clinicalIntegrationService.findVitalAssessByVisitId(visitId);
        String message = body == null ? MSG_VITAL_EMPTY_VISIT : MSG_VITAL_OK;
        return ResponseEntity.ok(new ApiResponse<>(true, message, body));
    }

    @Operation(
            summary = "접수 receptionId 기준 활력·문진 조회 (간호 상세 탭 권장)",
            description = "간호 기록에는 visitId가 없고 receptionId만 있을 때 사용. "
                    + "진료 지원이 receptionId에 맞는 visit를 고른 뒤 진료 API를 호출한다. "
                    + "HTTP는 데이터 유무와 관계없이 200이며, result가 null이면 프론트에서 빈 상태 UI를 보여 준다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "항상 200. 방문/문진 없음도 success=true, result=null",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "receptionId가 유효하지 않음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "502", description = "진료 MSA 연결 실패·진료 서버 오류")
    })
    @GetMapping("/reception/{receptionId}/vital-assess")
    public ResponseEntity<ApiResponse<ClinicalVitalAssessResponse>> findVitalAssessByReceptionId(
            @Parameter(description = "접수 ID (간호 기록 RECEPTION_ID)") @PathVariable Long receptionId
    ) {
        ClinicalVitalAssessResponse body = clinicalIntegrationService.findVitalAssessByReceptionId(receptionId);
        String message = body == null ? MSG_VITAL_EMPTY_RECEPTION : MSG_VITAL_OK;
        return ResponseEntity.ok(new ApiResponse<>(true, message, body));
    }
}
