package com.app.medical_support.common.integration.claims.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.common.integration.claims.dto.ClaimsDispatchResponse;
import com.app.medical_support.common.integration.claims.service.ClaimsDispatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/integration/claims")
@RequiredArgsConstructor
@Tag(name = "ClaimsIntegration", description = "수납 claims 연동 API")
public class ClaimsIntegrationController {

    private final ClaimsDispatchService claimsDispatchService;

    @Operation(summary = "환자 기준 visit resolve 후 claims 1건 전송")
    @PostMapping("/dispatch/patient/{patientId}")
    public ResponseEntity<ApiResponse<ClaimsDispatchResponse>> dispatchByPatientId(
            @Parameter(description = "환자 ID") @PathVariable Long patientId
    ) {
        ClaimsDispatchResponse result = claimsDispatchService.dispatchByPatientId(patientId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Claims dispatch by patient completed.", result));
    }

    @Operation(summary = "visit 기준 claims 1건 전송")
    @PostMapping("/dispatch/visit/{visitId}")
    public ResponseEntity<ApiResponse<ClaimsDispatchResponse>> dispatchByVisitId(
            @Parameter(description = "visit ID") @PathVariable Long visitId
    ) {
        ClaimsDispatchResponse result = claimsDispatchService.dispatchByVisitId(visitId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Claims dispatch by visit completed.", result));
    }
}
