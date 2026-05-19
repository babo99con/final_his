package com.app.medical_support.common.integration.reception.client;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.common.integration.reception.dto.OutpatientReceptionDTO;
import com.app.medical_support.common.integration.reception.dto.ReceptionDepartmentDTO;
import com.app.medical_support.common.integration.reception.dto.ReceptionDoctorDTO;
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
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class ReceptionApiClient {

    private static final String DEFAULT_BASE_URL = "http://localhost:8283";

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ReceptionApiClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${integration.reception.base-url:" + DEFAULT_BASE_URL + "}") String baseUrl
    ) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
        this.baseUrl = normalizeBaseUrl(baseUrl);
    }

    /**
     * 접수 MSA 진료과 마스터 ({@code GET /api/departments}).
     */
    public List<ReceptionDepartmentDTO> fetchDepartments() {
        String uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/api/departments")
                .toUriString();

        try {
            ResponseEntity<ApiResponse<List<ReceptionDepartmentDTO>>> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<List<ReceptionDepartmentDTO>>>() {
                    }
            );

            return unwrapListResult(responseEntity.getBody(), "Reception departments fetch failed.");
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reception departments request failed.", ex);
        } catch (HttpServerErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Reception service failed.", ex);
        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Reception service is unreachable.", ex);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Reception service call failed.", ex);
        }
    }

    /**
     * 접수 MSA 의사 목록 ({@code GET /api/doctors}). 진료 지원에서만 호출한다.
     *
     * @param departmentId 선택, 있으면 해당 진료과 소속 의사만
     */
    public List<ReceptionDoctorDTO> fetchDoctors(String departmentId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/api/doctors");
        if (trimToNull(departmentId) != null) {
            builder.queryParam("departmentId", departmentId.trim());
        }
        String uri = builder.toUriString();

        try {
            ResponseEntity<ApiResponse<List<ReceptionDoctorDTO>>> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<List<ReceptionDoctorDTO>>>() {
                    }
            );

            return unwrapListResult(responseEntity.getBody(), "Reception doctors fetch failed.");
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reception doctors request failed.", ex);
        } catch (HttpServerErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Reception service failed.", ex);
        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Reception service is unreachable.", ex);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Reception service call failed.", ex);
        }
    }

    public OutpatientReceptionDTO fetchDetail(Long id) {
        String uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/api/receptions/{id}")
                .buildAndExpand(id)
                .toUriString();

        try {
            ResponseEntity<ApiResponse<OutpatientReceptionDTO>> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<OutpatientReceptionDTO>>() {
                    }
            );

            return unwrapResult(responseEntity.getBody(), "Reception detail fetch failed.");
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reception not found. id=" + id, ex);
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Reception detail request failed. upstream="
                            + firstNonBlank(trimToNull(ex.getResponseBodyAsString()), ex.getMessage()),
                    ex
            );
        } catch (HttpServerErrorException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Reception service failed. upstream="
                            + firstNonBlank(trimToNull(ex.getResponseBodyAsString()), ex.getMessage()),
                    ex
            );
        } catch (ResourceAccessException ex) {
            OutpatientReceptionDTO fallback = findDetailFromQueueFallback(id);
            if (fallback != null) {
                return fallback;
            }
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Reception detail request timed out or service is unreachable.",
                    ex
            );
        } catch (RestClientException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Reception service call failed. reason=" + firstNonBlank(trimToNull(ex.getMessage()), "unknown"),
                    ex
            );
        }
    }

    /**
     * 접수 MSA 외래 접수 목록 ({@code GET /api/receptions}).
     * 진료 MSA {@code ReceptionClient#getReceptionQueue}와 동일하게 {@code dateFrom}/{@code dateTo}로 조회한다.
     */
    public List<OutpatientReceptionDTO> fetchReceptionsByDateRange(
            String dateFrom,
            String dateTo,
            String departmentId,
            String doctorId
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/api/receptions")
                .queryParam("dateFrom", dateFrom)
                .queryParam("dateTo", dateTo);
        if (trimToNull(departmentId) != null) {
            builder.queryParam("departmentId", departmentId.trim());
        }
        if (trimToNull(doctorId) != null) {
            builder.queryParam("doctorId", doctorId.trim());
        }
        String uri = builder.toUriString();

        try {
            ResponseEntity<ApiResponse<List<OutpatientReceptionDTO>>> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<List<OutpatientReceptionDTO>>>() {
                    }
            );

            return unwrapListResult(responseEntity.getBody(), "Reception list fetch failed.");
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reception list request failed.", ex);
        } catch (HttpServerErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Reception service failed.", ex);
        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Reception service is unreachable.", ex);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Reception service call failed.", ex);
        }
    }

    /**
     * 접수 MSA 외래 대기열 API 프록시.
     * {@code GET /api/receptions/queue?date=...&departmentId=...&doctorId=...}
     */
    public List<OutpatientReceptionDTO> fetchQueue(String date, String departmentId, String doctorId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/api/receptions/queue")
                .queryParam("date", date);
        if (trimToNull(departmentId) != null) {
            builder.queryParam("departmentId", departmentId.trim());
        }
        if (trimToNull(doctorId) != null) {
            builder.queryParam("doctorId", doctorId.trim());
        }
        String uri = builder.toUriString();

        try {
            ResponseEntity<ApiResponse<List<OutpatientReceptionDTO>>> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<List<OutpatientReceptionDTO>>>() {
                    }
            );

            return unwrapListResult(responseEntity.getBody(), "Reception queue fetch failed.");
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reception queue request failed.", ex);
        } catch (HttpServerErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Reception service failed.", ex);
        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Reception service is unreachable.", ex);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Reception service call failed.", ex);
        }
    }

    private <T> T unwrapResult(ApiResponse<T> response, String defaultMessage) {
        if (response == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Reception service response is empty.");
        }

        if (!response.isSuccess()) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, firstNonBlank(response.getMessage(), defaultMessage));
        }

        T result = response.getResult();
        if (result == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, defaultMessage);
        }

        return result;
    }

    private <T> List<T> unwrapListResult(ApiResponse<List<T>> response, String defaultMessage) {
        if (response == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Reception service response is empty.");
        }
        if (!response.isSuccess()) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, firstNonBlank(response.getMessage(), defaultMessage));
        }
        List<T> result = response.getResult();
        return result == null ? Collections.emptyList() : result;
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

    private OutpatientReceptionDTO findDetailFromQueueFallback(Long id) {
        if (id == null) {
            return null;
        }
        try {
            List<OutpatientReceptionDTO> queue = fetchQueue(LocalDate.now().toString(), null, null);
            return queue.stream()
                    .filter(item -> id.equals(item.getReceptionId()))
                    .findFirst()
                    .orElse(null);
        } catch (Exception ignored) {
            return null;
        }
    }
}
