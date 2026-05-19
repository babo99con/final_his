package app.auth.verification.service;

import app.auth.verification.repository.VerificationRepository;
import app.auth.verification.validator.VerificationValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class VerificationServiceImpl implements VerificationService {

    private static final Logger log = LoggerFactory.getLogger(VerificationServiceImpl.class);
    private static final int MAX_VERIFY_ATTEMPTS = 5;

    private final VerificationRepository verificationRepository;
    private final VerificationValidator verificationValidator;
    private final JavaMailSender mailSender;
    private final SecureRandom secureRandom = new SecureRandom();
    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.verify.email.mock:false}")
    private boolean emailMockMode;

    @Value("${app.verify.sms.mock:false}")
    private boolean smsMockMode;

    @Value("${app.verify.sms.provider-url:}")
    private String smsProviderUrl;

    @Value("${app.verify.sms.provider-auth-token:}")
    private String smsProviderAuthToken;

    @Value("${app.verify.sms.provider:custom}")
    private String smsProvider;

    @Value("${app.verify.sms.solapi.api-key:}")
    private String solapiApiKey;

    @Value("${app.verify.sms.solapi.api-secret:}")
    private String solapiApiSecret;

    @Value("${app.verify.sms.sender:}")
    private String smsSender;

    @Value("${app.verify.sms.message-template:[HMS] verification code: %s}")
    private String smsMessageTemplate;

    @Value("${spring.mail.username:}")
    private String mailFromAddress;

    public VerificationServiceImpl(VerificationRepository verificationRepository,
                                   VerificationValidator verificationValidator,
                                   JavaMailSender mailSender) {
        this.verificationRepository = verificationRepository;
        this.verificationValidator = verificationValidator;
        this.mailSender = mailSender;
    }

    @PostConstruct
    public void logVerificationConfig() {
        log.info(
            "[VERIFY_CONFIG] emailMock={}, smsMock={}, smsProvider={}, smsProviderUrl={}, solapiApiKeySet={}, solapiApiSecretSet={}, smsSenderSet={}",
            emailMockMode,
            smsMockMode,
            smsProvider,
            smsProviderUrl,
            StringUtils.hasText(solapiApiKey),
            StringUtils.hasText(solapiApiSecret),
            StringUtils.hasText(smsSender)
        );
    }

    @Override
    public String sendVerificationCode(String username, String email) {
        String normalizedUsername = verificationValidator.requireUsername(username);
        String normalizedEmail = verificationValidator.normalizeEmail(email);
        validateBoundEmail(normalizedUsername, normalizedEmail);
        validateEmailCooldown(normalizedUsername);

        String code = generateCode();
        verificationRepository.saveEmailCode(normalizedUsername, normalizedEmail, code);
        logEmailCode("AUTH_EMAIL_OTP", normalizedEmail, code);

        if (emailMockMode) {
            return "DEV email code: " + code;
        }

        sendEmailCode(normalizedEmail, code, "HMS email verification code");
        return "Verification email sent.";
    }

    @Override
    public boolean verifyCode(String username, String email, String code) {
        String normalizedUsername = verificationValidator.requireUsername(username);
        String normalizedEmail = verificationValidator.normalizeEmail(email);
        String normalizedCode = verificationValidator.normalizeCode(code);

        if (!isBoundEmailMatched(normalizedUsername, normalizedEmail)) {
            return false;
        }

        String storedValue = verificationRepository.readEmailCode(normalizedUsername);
        if (!StringUtils.hasText(storedValue) || !storedValue.contains("|")) {
            return false;
        }

        String[] parts = storedValue.split("\\|", 2);
        String storedEmail = parts[0];
        String storedCode = parts[1];

        if (normalizedEmail.equalsIgnoreCase(storedEmail) && normalizedCode.equals(storedCode)) {
            verificationRepository.clearEmailCode(normalizedUsername);
            verificationRepository.bindEmail(normalizedUsername, normalizedEmail);
            return true;
        }

        Long attempts = verificationRepository.incrementEmailAttempts(normalizedUsername);
        if (hasExceededAttempts(attempts)) {
            verificationRepository.clearEmailCode(normalizedUsername);
            throw new IllegalArgumentException("AUTH_EMAIL_CODE_ATTEMPTS_EXCEEDED");
        }

        return false;
    }

    @Override
    public String sendPhoneVerificationCode(String username, String phone) {
        String normalizedUsername = verificationValidator.requireUsername(username);
        String normalizedPhone = verificationValidator.normalizePhone(phone);
        validateBoundPhone(normalizedUsername, normalizedPhone);
        validatePhoneCooldown(normalizedUsername);

        String code = generateCode();
        verificationRepository.savePhoneCode(normalizedUsername, normalizedPhone, code);

        if (smsMockMode) {
            log.info("[AUTH_PHONE_OTP_MOCK] {} -> {}", normalizedPhone, code);
            return "DEV phone code: " + code;
        }

        if (!StringUtils.hasText(smsProviderUrl)) {
            throw new IllegalArgumentException("AUTH_SMS_NOT_CONFIGURED");
        }

        sendSmsCode(normalizedPhone, code);
        return "Verification sms sent.";
    }

    @Override
    public boolean verifyPhoneCode(String username, String phone, String code) {
        String normalizedUsername = verificationValidator.requireUsername(username);
        String normalizedPhone = verificationValidator.normalizePhone(phone);
        String normalizedCode = verificationValidator.normalizeCode(code);

        if (!isBoundPhoneMatched(normalizedUsername, normalizedPhone)) {
            return false;
        }

        String storedValue = verificationRepository.readPhoneCode(normalizedUsername);
        if (!StringUtils.hasText(storedValue) || !storedValue.contains("|")) {
            return false;
        }

        String[] parts = storedValue.split("\\|", 2);
        String storedPhone = parts[0];
        String storedCode = parts[1];

        if (normalizedPhone.equals(storedPhone) && normalizedCode.equals(storedCode)) {
            verificationRepository.clearPhoneCode(normalizedUsername);
            verificationRepository.bindPhone(normalizedUsername, normalizedPhone);
            return true;
        }

        Long attempts = verificationRepository.incrementPhoneAttempts(normalizedUsername);
        if (hasExceededAttempts(attempts)) {
            verificationRepository.clearPhoneCode(normalizedUsername);
            throw new IllegalArgumentException("AUTH_PHONE_CODE_ATTEMPTS_EXCEEDED");
        }

        return false;
    }

    @Override
    public String sendRegisterEmailCode(String rawEmail) {
        String email = verificationValidator.normalizeEmail(rawEmail);
        validateRegisterCooldown("email", email);

        String code = generateCode();
        verificationRepository.saveRegisterCode("email", email, code);
        logEmailCode("REGISTER_EMAIL_OTP", email, code);

        if (emailMockMode) {
            return "DEV register email code: " + code;
        }

        sendEmailCode(email, code, "HMS register email verification code");
        return "Email verification code sent.";
    }

    @Override
    public String verifyRegisterEmailCode(String rawEmail, String rawCode) {
        String email = verificationValidator.normalizeEmail(rawEmail);
        verifyRegisterCode("email", email, rawCode);
        return verificationRepository.issueVerifiedToken("email", email);
    }

    @Override
    public String sendRegisterPhoneCode(String rawPhone) {
        String phone = verificationValidator.normalizePhone(rawPhone);
        validateRegisterCooldown("phone", phone);

        String code = generateCode();

        if (smsMockMode) {
            verificationRepository.saveRegisterCode("phone", phone, code);
            log.info("[REGISTER_SMS_OTP_MOCK] {} -> {}", phone, code);
            return "DEV sms code: " + code;
        }

        if (!StringUtils.hasText(smsProviderUrl)) {
            throw new IllegalArgumentException("AUTH_SMS_NOT_CONFIGURED");
        }

        sendSmsCode(phone, code);
        verificationRepository.saveRegisterCode("phone", phone, code);
        return "SMS verification code sent.";
    }

    @Override
    public String verifyRegisterPhoneCode(String rawPhone, String rawCode) {
        String phone = verificationValidator.normalizePhone(rawPhone);
        verifyRegisterCode("phone", phone, rawCode);
        return verificationRepository.issueVerifiedToken("phone", phone);
    }

    private void validateBoundEmail(String username, String email) {
        String boundEmail = verificationRepository.getBoundEmail(username);
        if (StringUtils.hasText(boundEmail) && !boundEmail.equalsIgnoreCase(email)) {
            throw new IllegalArgumentException("AUTH_EMAIL_MISMATCH");
        }
    }

    private void validateBoundPhone(String username, String phone) {
        String boundPhone = verificationRepository.getBoundPhone(username);
        if (StringUtils.hasText(boundPhone) && !boundPhone.equals(phone)) {
            throw new IllegalArgumentException("AUTH_PHONE_MISMATCH");
        }
    }

    private boolean isBoundEmailMatched(String username, String email) {
        String boundEmail = verificationRepository.getBoundEmail(username);
        if (!StringUtils.hasText(boundEmail)) {
            return true;
        }

        return boundEmail.equalsIgnoreCase(email);
    }

    private boolean isBoundPhoneMatched(String username, String phone) {
        String boundPhone = verificationRepository.getBoundPhone(username);
        if (!StringUtils.hasText(boundPhone)) {
            return true;
        }
        return boundPhone.equals(phone);
    }

    private void validateEmailCooldown(String username) {
        if (verificationRepository.hasEmailCooldown(username)) {
            throw new IllegalArgumentException("AUTH_TOO_MANY_REQUESTS");
        }
    }

    private void validatePhoneCooldown(String username) {
        if (verificationRepository.hasPhoneCooldown(username)) {
            throw new IllegalArgumentException("AUTH_TOO_MANY_REQUESTS");
        }
    }

    private void validateRegisterCooldown(String type, String value) {
        if (verificationRepository.hasRegisterCooldown(type, value)) {
            throw new IllegalArgumentException("AUTH_TOO_MANY_REQUESTS");
        }
    }

    private void verifyRegisterCode(String type, String value, String rawCode) {
        String code = verificationValidator.normalizeCode(rawCode);
        String storedCode = verificationRepository.readRegisterCode(type, value);

        if (!StringUtils.hasText(storedCode)) {
            throw new IllegalArgumentException("AUTH_CODE_EXPIRED");
        }

        if (storedCode.equals(code)) {
            verificationRepository.clearRegisterCode(type, value);
            return;
        }

        Long attempts = verificationRepository.incrementRegisterAttempts(type, value);
        if (hasExceededAttempts(attempts)) {
            verificationRepository.clearRegisterCode(type, value);
            throw new IllegalArgumentException("AUTH_CODE_ATTEMPTS_EXCEEDED");
        }

        throw new IllegalArgumentException("AUTH_CODE_MISMATCH");
    }

    private void logEmailCode(String logPrefix, String target, String code) {
        String mode = "FALLBACK";
        if (emailMockMode) {
            mode = "MOCK";
        }

        log.info("[{}_{}] {} -> {}", logPrefix, mode, target, code);
    }

    private boolean hasExceededAttempts(Long attempts) {
        if (attempts == null) {
            return false;
        }

        return attempts >= MAX_VERIFY_ATTEMPTS;
    }

    private String generateCode() {
        int randomNumber = secureRandom.nextInt(900000);
        int codeNumber = randomNumber + 100000;
        return String.valueOf(codeNumber);
    }

    private void sendSmsCode(String phone, String code) {
        if ("solapi".equalsIgnoreCase(smsProvider)) {
            sendSmsCodeViaSolapiV4(phone, code);
            return;
        }

        try {
            String text = String.format(smsMessageTemplate, code);
            ObjectNode body = objectMapper.createObjectNode();
            body.put("to", phone);
            body.put("from", smsSender);
            body.put("text", text);
            body.put("message", text);
            body.put("content", text);
            body.put("code", code);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(smsProviderUrl))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json");

            if (StringUtils.hasText(smsProviderAuthToken)) {
                requestBuilder.header("Authorization", "Bearer " + smsProviderAuthToken.trim());
            }

            HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();

            if (statusCode < 200 || statusCode >= 300) {
                log.warn("[REGISTER_SMS_OTP_FAILED] status={}, body={}", statusCode, response.body());
                throw new IllegalArgumentException("AUTH_SMS_SEND_FAILED");
            }

            log.info("[REGISTER_SMS_OTP_SENT] phone={}", phone);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("[REGISTER_SMS_OTP_EXCEPTION] phone={}", phone, e);
            throw new IllegalArgumentException("AUTH_SMS_SEND_FAILED");
        }
    }

    private void sendSmsCodeViaSolapiV4(String phone, String code) {
        try {
            if (!StringUtils.hasText(solapiApiKey) || !StringUtils.hasText(solapiApiSecret)) {
                throw new IllegalArgumentException("AUTH_SMS_NOT_CONFIGURED");
            }
            if (!StringUtils.hasText(smsSender)) {
                throw new IllegalArgumentException("AUTH_SMS_SENDER_NOT_CONFIGURED");
            }

            String url = StringUtils.hasText(smsProviderUrl)
                ? smsProviderUrl
                : "https://api.solapi.com/messages/v4/send";
            String date = Instant.now().toString();
            String salt = UUID.randomUUID().toString().replace("-", "");
            String signature = hmacSha256Hex(date + salt, solapiApiSecret.trim());
            String text = String.format(smsMessageTemplate, code);

            ObjectNode body = objectMapper.createObjectNode();
            ObjectNode message = body.putObject("message");
            message.put("to", phone);
            message.put("from", smsSender.trim());
            message.put("text", text);

            String requestBody = objectMapper.writeValueAsString(body);

            String authHeaderPrimary =
                "HMAC-SHA256 apiKey=" + solapiApiKey.trim() +
                ", date=" + date +
                ", salt=" + salt +
                ", signature=" + signature;

            HttpResponse<String> primaryResponse = sendSolapiRequest(url, authHeaderPrimary, requestBody);
            if (isSuccess(primaryResponse.statusCode())) {
                log.info("[SOLAPI_SMS_SENT] phone={}", phone);
                return;
            }

            String primaryBody = primaryResponse.body();
            if (shouldRetryWithAlternateHeader(primaryBody)) {
                String authHeaderAlternate =
                    "HMAC-SHA256 ApiKey=" + solapiApiKey.trim() +
                    ", Date=" + date +
                    ", salt=" + salt +
                    ", signature=" + signature;
                HttpResponse<String> alternateResponse = sendSolapiRequest(url, authHeaderAlternate, requestBody);
                if (isSuccess(alternateResponse.statusCode())) {
                    log.info("[SOLAPI_SMS_SENT] phone={}", phone);
                    return;
                }
                String alternateBody = alternateResponse.body();
                log.warn("[SOLAPI_SMS_FAILED] status={}, body={}", alternateResponse.statusCode(), alternateBody);
                throw new IllegalArgumentException(resolveSolapiErrorCode(alternateBody));
            }

            log.warn("[SOLAPI_SMS_FAILED] status={}, body={}", primaryResponse.statusCode(), primaryBody);
            throw new IllegalArgumentException(resolveSolapiErrorCode(primaryBody));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("[SOLAPI_SMS_EXCEPTION] phone={}", phone, e);
            throw new IllegalArgumentException("AUTH_SMS_SEND_FAILED");
        }
    }

    private HttpResponse<String> sendSolapiRequest(String url, String authorization, String requestBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(10))
            .header("Content-Type", "application/json")
            .header("Authorization", authorization)
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private boolean isSuccess(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    private boolean shouldRetryWithAlternateHeader(String responseBody) {
        if (!StringUtils.hasText(responseBody)) {
            return false;
        }
        String upper = responseBody.toUpperCase();
        return upper.contains("SIGNATUREDOESNOTMATCH")
            || upper.contains("\"APIKEY\"")
            || upper.contains("API KEY");
    }

    private String hmacSha256Hex(String value, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKey);
        byte[] hashed = mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hashed);
    }

    private String md5Hex(String value) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] hashed = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hashed);
    }

    private String enc(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String resolveSolapiErrorCode(String responseBody) {
        try {
            String payload = responseBody == null ? "" : responseBody;
            if (!StringUtils.hasText(payload)) {
                return "AUTH_SMS_SEND_FAILED";
            }

            String errorCode = objectMapper.readTree(payload).path("errorCode").asText("");
            if (!StringUtils.hasText(errorCode)) {
                return "AUTH_SMS_SEND_FAILED";
            }

            String normalized = errorCode.toUpperCase();
            if ("INVALIDAPIKEY".equals(normalized)) {
                return "AUTH_SMS_INVALID_API_KEY";
            }
            if ("UNAUTHORIZED".equals(normalized) || "FORBIDDEN".equals(normalized)) {
                return "AUTH_SMS_UNAUTHORIZED";
            }
            if (normalized.contains("SENDER")) {
                return "AUTH_SMS_INVALID_SENDER";
            }
            if (normalized.contains("TOO_MANY")) {
                return "AUTH_TOO_MANY_REQUESTS";
            }

            return "AUTH_SMS_SEND_FAILED";
        } catch (Exception ignored) {
            return "AUTH_SMS_SEND_FAILED";
        }
    }

    private void sendEmailCode(String to, String code, String subject) {
        try {
            if (!StringUtils.hasText(mailFromAddress)) {
                throw new IllegalArgumentException("AUTH_EMAIL_NOT_CONFIGURED");
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFromAddress.trim());
            message.setTo(to);
            message.setSubject(subject);
            message.setText("[HMS] verification code: " + code);
            mailSender.send(message);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("[EMAIL_SEND_FAILED] to={}", to, e);
            throw new IllegalArgumentException("AUTH_EMAIL_SEND_FAILED");
        }
    }
}
