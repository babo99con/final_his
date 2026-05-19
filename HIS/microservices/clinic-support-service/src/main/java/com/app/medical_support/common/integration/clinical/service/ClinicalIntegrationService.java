package com.app.medical_support.common.integration.clinical.service;

import com.app.medical_support.common.integration.clinical.client.ClinicalApiClient;
import com.app.medical_support.common.integration.clinical.dto.ClinicalVitalAssessResponse;
import com.app.medical_support.common.integration.clinical.dto.ClinicalVisitSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClinicalIntegrationService {

    private static final String VISIT_STATUS_IN_PROGRESS = "IN_PROGRESS";

    private final ClinicalApiClient clinicalApiClient;

    public ClinicalVitalAssessResponse findVitalAssessByVisitId(Long visitId) {
        validatePositive("visitId", visitId);
        return clinicalApiClient.fetchVitalAssess(visitId);
    }

    /**
     * 간호 기록 등에는 receptionId만 있으므로, 진료 방문 목록에서 visitId를 고른 뒤 활력·문진을 조회한다.
     * visitId 선택 규칙(진료·기획과 동일하게 유지): (1) 설정된 단건 URL 템플릿이 있으면 우선 사용
     * (2) 같은 receptionId 중 visitStatus가 IN_PROGRESS 인 방이 있으면 그중 startTime 최신
     * (3) 없으면 같은 receptionId 전체 중 startTime 최신 (null startTime 은 맨 뒤)
     */
    public ClinicalVitalAssessResponse findVitalAssessByReceptionId(Long receptionId) {
        validatePositive("receptionId", receptionId);

        Long visitId = clinicalApiClient.tryFetchVisitIdByReceptionTemplate(receptionId);
        if (!isPositive(visitId)) {
            visitId = resolveVisitIdFromVisitList(receptionId);
        }
        if (visitId == null) {
            return null;
        }
        return clinicalApiClient.fetchVitalAssess(visitId);
    }

    /**
     * 수납 claims 집계를 위해 환자 기준으로 visitId를 선택한다.
     * 선택 규칙: IN_PROGRESS 방문 우선, 없으면 startTime 최신 방문.
     */
    public Long resolveVisitIdByPatientId(Long patientId) {
        validatePositive("patientId", patientId);
        List<ClinicalVisitSummaryResponse> visits = clinicalApiClient.fetchVisitsByPatientId(patientId).stream()
                .filter(item -> isPositive(item.getVisitId()))
                .collect(Collectors.toList());
        if (visits.isEmpty()) {
            return null;
        }
        Comparator<ClinicalVisitSummaryResponse> byStartTimeNewestFirst = Comparator.comparing(
                ClinicalVisitSummaryResponse::getStartTime,
                Comparator.nullsFirst(Comparator.naturalOrder())
        );
        return visits.stream()
                .filter(v -> VISIT_STATUS_IN_PROGRESS.equalsIgnoreCase(trimToEmpty(v.getVisitStatus())))
                .max(byStartTimeNewestFirst)
                .map(ClinicalVisitSummaryResponse::getVisitId)
                .orElseGet(() -> visits.stream().max(byStartTimeNewestFirst).map(ClinicalVisitSummaryResponse::getVisitId).orElse(null));
    }


    private Long resolveVisitIdFromVisitList(Long receptionId) {
        List<ClinicalVisitSummaryResponse> matches = clinicalApiClient.fetchVisitsByReceptionId(receptionId).stream()
                .filter(item -> isPositive(item.getVisitId()))
                .collect(Collectors.toList());
        if (matches.isEmpty()) {
            return null;
        }
        // ISO-8601 로컬 시각 문자열은 사전순이 곧 시간순. null 은 가장 이른 시각으로 취급해 max 시 최신만 선택.
        Comparator<ClinicalVisitSummaryResponse> byStartTimeNewestFirst = Comparator.comparing(
                ClinicalVisitSummaryResponse::getStartTime,
                Comparator.nullsFirst(Comparator.naturalOrder())
        );
        return matches.stream()
                .filter(v -> VISIT_STATUS_IN_PROGRESS.equalsIgnoreCase(trimToEmpty(v.getVisitStatus())))
                .max(byStartTimeNewestFirst)
                .map(ClinicalVisitSummaryResponse::getVisitId)
                .orElseGet(() -> matches.stream().max(byStartTimeNewestFirst).map(ClinicalVisitSummaryResponse::getVisitId).orElse(null));
    }

    private static String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private void validatePositive(String fieldName, Long value) {
        if (!isPositive(value)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " must be a positive number.");
        }
    }

    private boolean isPositive(Long value) {
        return value != null && value > 0;
    }
}
