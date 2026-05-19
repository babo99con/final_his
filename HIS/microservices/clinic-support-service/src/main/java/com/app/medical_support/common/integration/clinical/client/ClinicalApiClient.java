package com.app.medical_support.common.integration.clinical.client;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.common.integration.clinical.dto.ClinicalVitalAssessResponse;
import com.app.medical_support.common.integration.clinical.dto.ClinicalVisitSummaryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class ClinicalApiClient {

    private static final String DEFAULT_BASE_URL = "http://localhost:8090";

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String visitByReceptionUrlTemplate;
    /** (레거시) 전체 방문 목록 path. clinical_backend 저장소에는 없음 — reception 조회는 visitsByReceptionQueryPath 사용. */
    private final String visitsListPath;
    /**
     * 접수로 진료 방문 목록: GET {path}?receptionId= (clinical_backend 는 {@code /api/clinical}).
     */
    private final String visitsByReceptionQueryPath;

    public ClinicalApiClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${integration.clinical.base-url:" + DEFAULT_BASE_URL + "}") String baseUrl,
            @Value("${integration.clinical.visit-by-reception-url-template:}") String visitByReceptionUrlTemplate,
            @Value("${integration.clinical.visits-list-path:/api/visits}") String visitsListPath,
            @Value("${integration.clinical.visits-by-reception-query-path:/api/clinical}") String visitsByReceptionQueryPath
    ) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
        this.baseUrl = normalizeBaseUrl(baseUrl);
        this.visitByReceptionUrlTemplate = trimToNull(visitByReceptionUrlTemplate);
        this.visitsListPath = normalizePath(trimToNull(visitsListPath) != null ? visitsListPath : "/api/visits");
        this.visitsByReceptionQueryPath = normalizePath(trimToNull(visitsByReceptionQueryPath) != null ? visitsByReceptionQueryPath : "/api/clinical");
    }

    /**
     * 진료 MSA가 접수→visit 단건 조회를 제공할 때 사용. 템플릿이 비어 있으면 null.
     * 템플릿 예: http://localhost:8090/api/visits/by-reception/{receptionId}/visit-id
     * 응답은 ApiResponse이고 result가 숫형 visitId이거나 {"visitId": n} 객체일 수 있다.
     */
    public Long tryFetchVisitIdByReceptionTemplate(Long receptionId) {
        if (visitByReceptionUrlTemplate == null) {
            return null;
        }
        String uri = visitByReceptionUrlTemplate.replace("{receptionId}", String.valueOf(receptionId));
        try {
            ResponseEntity<ApiResponse<Object>> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<Object>>() {
                    }
            );
            return extractVisitIdFromApiResult(unwrapResultObject(responseEntity.getBody(), "Clinical visit id by reception fetch failed."));
        } catch (HttpClientErrorException.NotFound ex) {
            return null;
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clinical visit-by-reception request failed.", ex);
        } catch (HttpServerErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service failed.", ex);
        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service is unreachable.", ex);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service call failed.", ex);
        }
    }

    /**
     * 접수 ID로 진료 방문 목록 (진료: GET /api/clinical?receptionId=…).
     */
    public List<ClinicalVisitSummaryResponse> fetchVisitsByReceptionId(Long receptionId) {
        String uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(visitsByReceptionQueryPath)
                .queryParam("receptionId", receptionId)
                .toUriString();
        try {
            ResponseEntity<ApiResponse<List<ClinicalVisitSummaryResponse>>> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<List<ClinicalVisitSummaryResponse>>>() {
                    }
            );
            return unwrapList(responseEntity.getBody(), "Clinical visits by reception fetch failed.");
        } catch (HttpClientErrorException ex) {
            String detail = clinicalErrorDetail(ex);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Clinical visits-by-reception request failed (GET " + uri + "). " + detail,
                    ex);
        } catch (HttpServerErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service failed.", ex);
        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service is unreachable.", ex);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service call failed.", ex);
        }
    }

    /**
     * 환자 ID로 진료 방문 목록 조회 (clinical: GET /api/clinical?patientId=...).
     */
    public List<ClinicalVisitSummaryResponse> fetchVisitsByPatientId(Long patientId) {
        String uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(visitsByReceptionQueryPath)
                .queryParam("patientId", patientId)
                .toUriString();
        try {
            ResponseEntity<ApiResponse<List<ClinicalVisitSummaryResponse>>> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<List<ClinicalVisitSummaryResponse>>>() {
                    }
            );
            return unwrapList(responseEntity.getBody(), "Clinical visits by patient fetch failed.");
        } catch (HttpClientErrorException ex) {
            String detail = clinicalErrorDetail(ex);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Clinical visits-by-patient request failed (GET " + uri + "). " + detail,
                    ex);
        } catch (HttpServerErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service failed.", ex);
        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service is unreachable.", ex);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service call failed.", ex);
        }
    }


    public List<ClinicalVisitSummaryResponse> fetchVisitList() {
        String uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(visitsListPath)
                .toUriString();
        try {
            ResponseEntity<ApiResponse<List<ClinicalVisitSummaryResponse>>> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<List<ClinicalVisitSummaryResponse>>>() {
                    }
            );
            return unwrapList(responseEntity.getBody(), "Clinical visit list fetch failed.");
        } catch (HttpClientErrorException ex) {
            String detail = clinicalErrorDetail(ex);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Clinical visit list request failed (GET " + uri + "). " + detail,
                    ex);
        } catch (HttpServerErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service failed.", ex);
        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service is unreachable.", ex);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service call failed.", ex);
        }
    }

    public ClinicalVitalAssessResponse fetchVitalAssess(Long visitId) {
        String uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/api/visits/{visitId}/vital-assess")
                .buildAndExpand(visitId)
                .toUriString();
        try {
            ResponseEntity<ApiResponse<ClinicalVitalAssessResponse>> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<ClinicalVitalAssessResponse>>() {
                    }
            );
            return unwrapAllowNull(responseEntity.getBody(), "Clinical vital assess fetch failed.");
        } catch (HttpClientErrorException.NotFound ex) {
            return null;
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clinical vital assess request failed.", ex);
        } catch (HttpServerErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service failed.", ex);
        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service is unreachable.", ex);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service call failed.", ex);
        }
    }

    private List<ClinicalVisitSummaryResponse> unwrapList(
            ApiResponse<List<ClinicalVisitSummaryResponse>> response,
            String defaultMessage
    ) {
        if (response == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service response is empty.");
        }
        if (!response.isSuccess()) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, firstNonBlank(response.getMessage(), defaultMessage));
        }
        List<ClinicalVisitSummaryResponse> result = response.getResult();
        return result == null ? Collections.emptyList() : result;
    }

    private ClinicalVitalAssessResponse unwrapAllowNull(
            ApiResponse<ClinicalVitalAssessResponse> response,
            String defaultMessage
    ) {
        if (response == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service response is empty.");
        }
        if (!response.isSuccess()) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, firstNonBlank(response.getMessage(), defaultMessage));
        }
        return response.getResult();
    }

    private String normalizeBaseUrl(String value) {
        String normalized = trimToNull(value);
        if (normalized == null) {
            return DEFAULT_BASE_URL;
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private static String normalizePath(String path) {
        if (path == null || path.isEmpty()) {
            return "/api/visits";
        }
        return path.startsWith("/") ? path : "/" + path;
    }

    private String firstNonBlank(String first, String second) {
        String firstValue = trimToNull(first);
        if (firstValue != null) {
            return firstValue;
        }
        return trimToNull(second);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private Object unwrapResultObject(ApiResponse<Object> response, String defaultMessage) {
        if (response == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Clinical service response is empty.");
        }
        if (!response.isSuccess()) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, firstNonBlank(response.getMessage(), defaultMessage));
        }
        return response.getResult();
    }

    private Long extractVisitIdFromApiResult(Object result) {
        if (result == null) {
            return null;
        }
        if (result instanceof Number) {
            return ((Number) result).longValue();
        }
        if (result instanceof Map<?, ?>) {
            Object v = ((Map<?, ?>) result).get("visitId");
            if (v instanceof Number) {
                return ((Number) v).longValue();
            }
        }
        return null;
    }

    private static String clinicalErrorDetail(HttpClientErrorException ex) {
        StringBuilder sb = new StringBuilder();
        sb.append("upstream HTTP ").append(ex.getRawStatusCode());
        String body = ex.getResponseBodyAsString();
        if (body != null && !body.isBlank()) {
            String trimmed = body.trim().replaceAll("\\s+", " ");
            if (trimmed.length() > 240) {
                trimmed = trimmed.substring(0, 240) + "...";
            }
            sb.append(" body=").append(trimmed);
        }
        return sb.toString();
    }
}
