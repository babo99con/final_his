package com.hospital.billing.toss.client;

import com.hospital.billing.toss.config.TossPaymentProperties;
import com.hospital.billing.toss.dto.TossApproveRequest;
import com.hospital.billing.toss.dto.TossApproveResponse;
import com.hospital.billing.toss.dto.TossCancelRequest;
import com.hospital.billing.toss.dto.TossCancelResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TossPaymentClient {

    private static final Logger log = LoggerFactory.getLogger(TossPaymentClient.class);

    private final TossPaymentProperties tossPaymentProperties;
    private final RestTemplate restTemplate;

    public TossPaymentClient(TossPaymentProperties tossPaymentProperties) {
        this.tossPaymentProperties = tossPaymentProperties;
        this.restTemplate = new RestTemplate();
    }

    public TossApproveResponse confirmPayment(TossApproveRequest request) {
        String url = tossPaymentProperties.getBaseUrl() + "/v1/payments/confirm";

        log.info("[toss] confirmPayment start");
        log.info("[toss] url={}", url);
        log.info("[toss] orderId={}", request.getOrderId());
        log.info("[toss] amount={}", request.getAmount());
        log.info("[toss] paymentKey={}", maskPaymentKey(request.getPaymentKey()));
        log.info("[toss] secretKeyLoaded={}",
                tossPaymentProperties.getSecretKey() != null && !tossPaymentProperties.getSecretKey().isBlank());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(createEncodedSecretKey());

        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey", request.getPaymentKey());
        body.put("orderId", request.getOrderId());
        body.put("amount", request.getAmount());

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpEntity,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();

            log.info("[toss] confirmPayment success status={}", response.getStatusCode());

            if (responseBody == null) {
                throw new RuntimeException("토스 승인 응답이 비어 있습니다.");
            }

            TossApproveResponse approveResponse = new TossApproveResponse();
            approveResponse.setPaymentKey((String) responseBody.get("paymentKey"));
            approveResponse.setOrderId((String) responseBody.get("orderId"));
            approveResponse.setStatus((String) responseBody.get("status"));
            approveResponse.setMethod((String) responseBody.get("method"));

            Object totalAmount = responseBody.get("totalAmount");
            if (totalAmount instanceof Number number) {
                approveResponse.setAmount(number.longValue());
            }

            return approveResponse;

        } catch (HttpStatusCodeException e) {
            log.error("[toss] confirmPayment http error status={}", e.getStatusCode());
            log.error("[toss] confirmPayment response body={}", e.getResponseBodyAsString());
            log.error("[toss] confirmPayment response headers={}", e.getResponseHeaders());
            throw wrapTossHttpError("승인(confirm)", e);
        } catch (Exception e) {
            log.error("[toss] confirmPayment unexpected error", e);
            throw e;
        }
    }

    public TossCancelResponse cancelPayment(TossCancelRequest request) {
        String url = tossPaymentProperties.getBaseUrl()
                + "/v1/payments/"
                + request.getPaymentKey()
                + "/cancel";

        log.info("[toss] cancelPayment start");
        log.info("[toss] url={}", url);
        log.info("[toss] paymentKey={}", maskPaymentKey(request.getPaymentKey()));
        log.info("[toss] cancelAmount={}", request.getCancelAmount());
        log.info("[toss] secretKeyLoaded={}",
                tossPaymentProperties.getSecretKey() != null && !tossPaymentProperties.getSecretKey().isBlank());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(createEncodedSecretKey());

        Map<String, Object> body = new HashMap<>();
        body.put("cancelReason", request.getCancelReason());

        if (request.getCancelAmount() != null && request.getCancelAmount() > 0) {
            body.put("cancelAmount", request.getCancelAmount());
        }

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpEntity,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();

            log.info("[toss] cancelPayment success status={}", response.getStatusCode());

            if (responseBody == null) {
                throw new RuntimeException("토스 취소 응답이 비어 있습니다.");
            }

            TossCancelResponse cancelResponse = new TossCancelResponse();
            cancelResponse.setPaymentKey((String) responseBody.get("paymentKey"));
            cancelResponse.setOrderId((String) responseBody.get("orderId"));
            cancelResponse.setStatus((String) responseBody.get("status"));
            cancelResponse.setMethod((String) responseBody.get("method"));

            Object totalAmount = responseBody.get("totalAmount");
            if (totalAmount instanceof Number number) {
                cancelResponse.setTotalAmount(number.longValue());
            }

            Object cancelsObj = responseBody.get("cancels");
            if (cancelsObj instanceof List<?> cancels && !cancels.isEmpty()) {
                Object firstCancel = cancels.get(0);
                if (firstCancel instanceof Map<?, ?> cancelMap) {
                    Object cancelAmount = cancelMap.get("cancelAmount");
                    if (cancelAmount instanceof Number number) {
                        cancelResponse.setCanceledAmount(number.longValue());
                    }
                }
            }

            return cancelResponse;

        } catch (HttpStatusCodeException e) {
            log.error("[toss] cancelPayment http error status={}", e.getStatusCode());
            log.error("[toss] cancelPayment response body={}", e.getResponseBodyAsString());
            log.error("[toss] cancelPayment response headers={}", e.getResponseHeaders());
            throw wrapTossHttpError("취소(cancel)", e);
        } catch (Exception e) {
            log.error("[toss] cancelPayment unexpected error", e);
            throw e;
        }
    }

    private String createEncodedSecretKey() {
        String secretKey = tossPaymentProperties.getSecretKey();
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException(
                    "toss.secret-key 가 비어 있습니다. billing-service 실행 환경에 TOSS_SECRET_KEY(토스 시크릿 키)를 설정하세요. "
                            + "프론트의 NEXT_PUBLIC_TOSS_CLIENT_KEY(test_ck_...)와 같은 매장의 test_sk_/live 키 쌍이어야 승인 API가 동작합니다.");
        }
        String value = secretKey + ":";
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String maskPaymentKey(String paymentKey) {
        if (paymentKey == null || paymentKey.length() < 8) {
            return paymentKey;
        }
        return paymentKey.substring(0, 6) + "..." + paymentKey.substring(paymentKey.length() - 4);
    }

    /**
     * RestTemplate 기본 메시지({@code 500 Internal Server Error: [no body]})만으로는 원인 파악이 어려워
     * HTTP 상태·본문·URL을 묶어 전달한다.
     */
    private static IllegalStateException wrapTossHttpError(String phase, HttpStatusCodeException e) {
        int status = e.getStatusCode().value();
        String body = e.getResponseBodyAsString();
        String traceId = extractTossTraceId(e);
        String detail = (body == null || body.isBlank())
                ? "(응답 본문 없음 — 토스 측 일시 장애·프록시·방화벽 가능성. 아래 trace-id로 토스페이먼츠 문의)"
                : body;
        if (detail.length() > 800) {
            detail = detail.substring(0, 800) + "...";
        }
        String suffix = traceId != null ? " x-tosspayments-trace-id=" + traceId : "";
        String msg = String.format(
                "토스 결제 %s API 오류 (HTTP %d). %s%s",
                phase,
                status,
                detail,
                suffix
        );
        return new IllegalStateException(msg, e);
    }

    private static String extractTossTraceId(HttpStatusCodeException e) {
        if (e.getResponseHeaders() == null) {
            return null;
        }
        List<String> ids = e.getResponseHeaders().get("x-tosspayments-trace-id");
        if (ids == null || ids.isEmpty()) {
            return null;
        }
        String id = ids.get(0);
        return id != null && !id.isBlank() ? id.trim() : null;
    }
}