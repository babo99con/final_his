package com.app.medical_support.common.integration.claims.service;

import com.app.medical_support.common.integration.claims.client.ClaimsApiClient;
import com.app.medical_support.common.integration.claims.dto.ClaimsDispatchResponse;
import com.app.medical_support.common.integration.claims.dto.ClaimsRequest;
import com.app.medical_support.common.integration.claims.dto.ClaimsResultResponse;
import com.app.medical_support.common.integration.clinical.service.ClinicalIntegrationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class ClaimsDispatchService {

    private static final DateTimeFormatter OCCURRED_AT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final String STATUS_COMPLETED = "COMPLETED";

    private final ClaimsApiClient claimsApiClient;
    private final ClaimsStagingService claimsStagingService;
    private final ClinicalIntegrationService clinicalIntegrationService;

    public ClaimsDispatchService(
            ClaimsApiClient claimsApiClient,
            ClaimsStagingService claimsStagingService,
            ClinicalIntegrationService clinicalIntegrationService
    ) {
        this.claimsApiClient = claimsApiClient;
        this.claimsStagingService = claimsStagingService;
        this.clinicalIntegrationService = clinicalIntegrationService;
    }

    @Transactional
    public ClaimsDispatchResponse dispatchByPatientId(Long patientId) {
        Long visitId = clinicalIntegrationService.resolveVisitIdByPatientId(patientId);
        if (visitId == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "visitId를 찾을 수 없습니다. patientId=" + patientId);
        }
        return dispatchByVisitId(visitId);
    }

    @Transactional
    public ClaimsDispatchResponse dispatchByVisitId(Long visitId) {
        ClaimsStagingService.VisitClaimsStage stage = claimsStagingService.snapshot(visitId);
        if (stage == null || stage.getItems().isEmpty()) {
            return ClaimsDispatchResponse.builder()
                    .visitId(visitId)
                    .patientId(stage == null ? null : stage.getPatientId())
                    .itemCount(0)
                    .dispatched(false)
                    .alreadyProcessed(false)
                    .message("전송할 staging item이 없습니다.")
                    .build();
        }
        String eventId = UUID.randomUUID().toString();
        ClaimsRequest request = ClaimsRequest.builder()
                .eventId(eventId)
                .visitId(stage.getVisitId())
                .patientId(stage.getPatientId())
                .status(STATUS_COMPLETED)
                .occurredAt(LocalDateTime.now().format(OCCURRED_AT_FORMAT))
                .items(stage.getItems())
                .build();

        ClaimsResultResponse result = claimsApiClient.createClaims(request);
        claimsStagingService.markDispatched(visitId, eventId);
        return ClaimsDispatchResponse.builder()
                .visitId(visitId)
                .patientId(stage.getPatientId())
                .eventId(eventId)
                .itemCount(stage.getItems().size())
                .dispatched(true)
                .alreadyProcessed(result.isAlreadyProcessed())
                .message(result.getMessage())
                .build();
    }
}
