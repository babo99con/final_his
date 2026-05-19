package com.app.medical_support.common.integration.claims.service;

import com.app.medical_support.common.integration.claims.dto.ClaimsItemRequest;
import com.app.medical_support.common.integration.clinical.service.ClinicalIntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClaimsCompletionStageService {

    private static final String SOURCE_TYPE = "CLINICAL_ORDER_ITEM";

    private final ClinicalIntegrationService clinicalIntegrationService;
    private final ClaimsStagingService claimsStagingService;

    public void stageMedicationCompleted(Long patientId, String medicationRecordId, String medicationId) {
        stage(patientId, medicationRecordId, "PRESCRIPTION", "투약: " + safeName(medicationId));
    }

    public void stageTreatmentCompleted(Long patientId, String treatmentResultId, String detail) {
        stage(patientId, treatmentResultId, "PROCEDURE", "처치: " + safeName(detail));
    }

    public void stageDiagnosticCompleted(Long patientId, String resultType, String resultId) {
        String orderType = switch (normalize(resultType)) {
            case "IMAGING", "ENDOSCOPY" -> "IMAGING";
            case "SPECIMEN", "PATHOLOGY", "PHYSIOLOGICAL" -> "LAB";
            default -> "LAB";
        };
        stage(patientId, resultId, orderType, normalize(resultType) + " 결과 완료");
    }

    private void stage(Long patientId, String sourceRawId, String orderType, String itemName) {
        if (patientId == null || patientId <= 0) {
            return;
        }
        try {
            Long visitId = clinicalIntegrationService.resolveVisitIdByPatientId(patientId);
            if (visitId == null) {
                return;
            }
            claimsStagingService.stageItem(
                    visitId,
                    patientId,
                    ClaimsItemRequest.builder()
                            .itemName(itemName)
                            .itemCode(null)
                            .orderType(orderType)
                            .sourceId(toNumericSourceId(sourceRawId))
                            .sourceType(SOURCE_TYPE)
                            .build()
            );
        } catch (RuntimeException ex) {
            log.warn(
                    "Claims staging skipped (clinical visit resolve failed). patientId={} orderType={} itemName={} : {}",
                    patientId,
                    orderType,
                    itemName,
                    ex.getMessage()
            );
        }
    }

    private Long toNumericSourceId(String sourceRawId) {
        if (sourceRawId == null || sourceRawId.trim().isEmpty()) {
            return null;
        }
        String onlyDigits = sourceRawId.replaceAll("[^0-9]", "");
        if (!onlyDigits.isEmpty()) {
            try {
                return Long.parseLong(onlyDigits);
            } catch (NumberFormatException ignored) {
                // fall through
            }
        }
        return Math.abs((long) sourceRawId.hashCode());
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase();
    }

    private String safeName(String value) {
        return (value == null || value.trim().isEmpty()) ? "-" : value.trim();
    }
}
