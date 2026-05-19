package com.app.medical_support.common.integration.claims.client;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.common.integration.claims.dto.ClaimsRequest;
import com.app.medical_support.common.integration.claims.dto.ClaimsResultResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
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

@Component
public class ClaimsApiClient {

    private static final String DEFAULT_BASE_URL = "http://localhost:8081";

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String claimsPath;

    public ClaimsApiClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${integration.billing.base-url:" + DEFAULT_BASE_URL + "}") String baseUrl,
            @Value("${integration.billing.claims-path:/api/billing/claims}") String claimsPath
    ) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(7))
                .build();
        this.baseUrl = normalizeBaseUrl(baseUrl);
        this.claimsPath = normalizePath(claimsPath);
    }

    public ClaimsResultResponse createClaims(ClaimsRequest request) {
        String uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(claimsPath)
                .toUriString();
        try {
            ResponseEntity<ApiResponse<ClaimsResultResponse>> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    new ParameterizedTypeReference<ApiResponse<ClaimsResultResponse>>() {
                    }
            );
            ApiResponse<ClaimsResultResponse> body = responseEntity.getBody();
            if (body == null || !body.isSuccess() || body.getResult() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Billing claims response is invalid.");
            }
            return body.getResult();
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Billing claims request failed.", ex);
        } catch (HttpServerErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Billing service failed.", ex);
        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Billing service is unreachable.", ex);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Billing service call failed.", ex);
        }
    }

    private String normalizeBaseUrl(String value) {
        String normalized = value == null ? null : value.trim();
        if (normalized == null || normalized.isEmpty()) {
            return DEFAULT_BASE_URL;
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private String normalizePath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return "/api/billing/claims";
        }
        String trimmed = path.trim();
        return trimmed.startsWith("/") ? trimmed : "/" + trimmed;
    }
}
