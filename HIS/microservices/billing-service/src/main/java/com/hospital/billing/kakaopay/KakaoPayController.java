package com.hospital.billing.kakaopay;

import com.fasterxml.jackson.databind.JsonNode;
import com.hospital.billing.entity.Bill;
import com.hospital.billing.entity.BillingStatus;
import com.hospital.billing.payment.entity.PaymentMethod;
import com.hospital.billing.payment.service.PaymentService;
import com.hospital.billing.repository.BillRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/billing/kakaopay")
public class KakaoPayController {
    private static final String TEST_CID = "TC0ONETIME";
    private static final String PARTNER_USER = "his-sandbox";
    private final BillRepository bills;
    private final PaymentService payments;
    private final JdbcTemplate jdbc;
    private final RestClient kakao;
    private final String secret;
    private final String webOrigin;

    public KakaoPayController(
            BillRepository bills, PaymentService payments, JdbcTemplate jdbc,
            @Value("${kakaopay.secret-key:}") String secret,
            @Value("${kakaopay.web-origin:http://localhost:3001}") String webOrigin
    ) {
        this.bills = bills;
        this.payments = payments;
        this.jdbc = jdbc;
        this.secret = secret;
        this.webOrigin = webOrigin;
        this.kakao = RestClient.create("https://open-api.kakaopay.com");
    }

    @PostMapping("/ready/{billId}")
    @Transactional
    public Ready ready(@PathVariable Long billId) {
        Bill bill = bills.findById(billId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "청구를 찾을 수 없습니다."));
        int amount = requireBalance(bill);
        String order = UUID.randomUUID().toString();
        String callback = webOrigin + "/billing/kakaopay";
        JsonNode result = call("ready", Map.ofEntries(
                Map.entry("cid", TEST_CID),
                Map.entry("partner_order_id", order),
                Map.entry("partner_user_id", PARTNER_USER),
                Map.entry("item_name", "HIS demo payment"),
                Map.entry("quantity", 1),
                Map.entry("total_amount", amount),
                Map.entry("tax_free_amount", 0),
                Map.entry("approval_url", callback + "?order=" + order),
                Map.entry("cancel_url", callback + "?state=cancel"),
                Map.entry("fail_url", callback + "?state=fail")
        ));
        String tid = result.path("tid").asText();
        String redirect = result.path("next_redirect_pc_url").asText();
        if (tid.isBlank() || !"https".equals(URI.create(redirect).getScheme())) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "카카오페이 응답을 확인할 수 없습니다.");
        }
        jdbc.update("INSERT INTO KAKAOPAY_ATTEMPT (ORDER_ID, BILL_ID, TID, AMOUNT) VALUES (?, ?, ?, ?)",
                order, billId, tid, amount);
        return new Ready(redirect);
    }

    @PostMapping("/approve")
    @Transactional
    public Approved approve(@RequestBody Approval approval) {
        if (approval.order() == null || approval.pgToken() == null || approval.pgToken().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "결제 승인 정보가 필요합니다.");
        }
        Attempt attempt = jdbc.query("""
                SELECT BILL_ID, TID, AMOUNT, RECORDED
                FROM KAKAOPAY_ATTEMPT WHERE ORDER_ID = ? FOR UPDATE
                """, rows -> rows.next()
                ? new Attempt(rows.getLong(1), rows.getString(2), rows.getInt(3), rows.getInt(4) == 1)
                : null, approval.order());
        if (attempt == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "결제 요청을 찾을 수 없습니다.");
        }
        if (attempt.recorded()) {
            return new Approved(attempt.billId());
        }
        Bill bill = bills.findForPayment(attempt.billId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (requireBalance(bill) != attempt.amount()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "청구 잔액이 변경되었습니다.");
        }

        JsonNode result;
        try {
            result = call("approve", Map.of(
                    "cid", TEST_CID, "tid", attempt.tid(),
                    "partner_order_id", approval.order(),
                    "partner_user_id", PARTNER_USER, "pg_token", approval.pgToken()));
        } catch (ResponseStatusException ex) {
            if (ex.getStatusCode() != HttpStatus.BAD_GATEWAY) {
                throw ex;
            }
            result = call("order", Map.of("cid", TEST_CID, "tid", attempt.tid()));
            if (!"SUCCESS_PAYMENT".equals(result.path("status").asText())) {
                throw ex;
            }
        }
        if (!attempt.tid().equals(result.path("tid").asText())
                || !approval.order().equals(result.path("partner_order_id").asText())
                || result.path("amount").path("total").asInt(-1) != attempt.amount()) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "승인 거래와 청구 정보가 일치하지 않습니다.");
        }
        payments.createPayment(attempt.billId(), attempt.amount(), PaymentMethod.KAKAOPAY,
                attempt.tid(), approval.order());
        jdbc.update("UPDATE KAKAOPAY_ATTEMPT SET RECORDED = 1 WHERE ORDER_ID = ?", approval.order());
        return new Approved(attempt.billId());
    }

    private int requireBalance(Bill bill) {
        if (bill.getStatus() == BillingStatus.CANCELED || bill.getRemainingAmount() == null
                || bill.getRemainingAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "결제할 청구 잔액이 없습니다.");
        }
        return bill.getRemainingAmount();
    }

    private JsonNode call(String action, Map<String, ?> body) {
        if (secret.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "KAKAOPAY_SECRET_KEY 환경 변수를 설정해 주세요.");
        }
        try {
            JsonNode response = kakao.post().uri("/online/v1/payment/" + action)
                    .header("Authorization", "SECRET_KEY " + secret)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(body).retrieve().body(JsonNode.class);
            if (response != null) {
                return response;
            }
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "카카오페이 요청에 실패했습니다.");
        }
        throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "카카오페이 응답이 비어 있습니다.");
    }

    private record Attempt(long billId, String tid, int amount, boolean recorded) {}
    public record Approval(String order, String pgToken) {}
    public record Ready(String redirectUrl) {}
    public record Approved(long billId) {}
}
