package app.auth.verification.repository;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
public class VerificationRepository {

    private static final long CODE_TTL_MINUTES = 5;
    private static final long VERIFY_TOKEN_TTL_MINUTES = 30;
    private static final long RESEND_COOLDOWN_SECONDS = 60;
    private static final String EMAIL_VERIFY_KEY_PREFIX = "auth:email:verify:";
    private static final String EMAIL_BIND_KEY_PREFIX = "auth:email:bind:";
    private static final String EMAIL_COOLDOWN_KEY_PREFIX = "auth:email:cooldown:";
    private static final String EMAIL_ATTEMPT_KEY_PREFIX = "auth:email:attempt:";
    private static final String PHONE_VERIFY_KEY_PREFIX = "auth:phone:verify:";
    private static final String PHONE_BIND_KEY_PREFIX = "auth:phone:bind:";
    private static final String PHONE_COOLDOWN_KEY_PREFIX = "auth:phone:cooldown:";
    private static final String PHONE_ATTEMPT_KEY_PREFIX = "auth:phone:attempt:";
    private static final String REGISTER_KEY_PREFIX = "auth:register:";

    private final StringRedisTemplate redisTemplate;

    public VerificationRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getBoundEmail(String username) {
        return redisTemplate.opsForValue().get(EMAIL_BIND_KEY_PREFIX + username);
    }

    public void bindEmail(String username, String email) {
        redisTemplate.opsForValue().set(EMAIL_BIND_KEY_PREFIX + username, email);
    }

    public boolean hasEmailCooldown(String username) {
        return StringUtils.hasText(redisTemplate.opsForValue().get(EMAIL_COOLDOWN_KEY_PREFIX + username));
    }

    public void saveEmailCode(String username, String email, String code) {
        redisTemplate.opsForValue().set(EMAIL_VERIFY_KEY_PREFIX + username, email + "|" + code, CODE_TTL_MINUTES, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(EMAIL_COOLDOWN_KEY_PREFIX + username, "1", RESEND_COOLDOWN_SECONDS, TimeUnit.SECONDS);
    }

    public String readEmailCode(String username) {
        return redisTemplate.opsForValue().get(EMAIL_VERIFY_KEY_PREFIX + username);
    }

    public Long incrementEmailAttempts(String username) {
        Long value = redisTemplate.opsForValue().increment(EMAIL_ATTEMPT_KEY_PREFIX + username);
        redisTemplate.expire(EMAIL_ATTEMPT_KEY_PREFIX + username, CODE_TTL_MINUTES, TimeUnit.MINUTES);
        return value;
    }

    public void clearEmailCode(String username) {
        redisTemplate.delete(EMAIL_VERIFY_KEY_PREFIX + username);
        redisTemplate.delete(EMAIL_ATTEMPT_KEY_PREFIX + username);
    }

    public String getBoundPhone(String username) {
        return redisTemplate.opsForValue().get(PHONE_BIND_KEY_PREFIX + username);
    }

    public void bindPhone(String username, String phone) {
        redisTemplate.opsForValue().set(PHONE_BIND_KEY_PREFIX + username, phone);
    }

    public boolean hasPhoneCooldown(String username) {
        return StringUtils.hasText(redisTemplate.opsForValue().get(PHONE_COOLDOWN_KEY_PREFIX + username));
    }

    public void savePhoneCode(String username, String phone, String code) {
        redisTemplate.opsForValue().set(PHONE_VERIFY_KEY_PREFIX + username, phone + "|" + code, CODE_TTL_MINUTES, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(PHONE_COOLDOWN_KEY_PREFIX + username, "1", RESEND_COOLDOWN_SECONDS, TimeUnit.SECONDS);
    }

    public String readPhoneCode(String username) {
        return redisTemplate.opsForValue().get(PHONE_VERIFY_KEY_PREFIX + username);
    }

    public Long incrementPhoneAttempts(String username) {
        Long value = redisTemplate.opsForValue().increment(PHONE_ATTEMPT_KEY_PREFIX + username);
        redisTemplate.expire(PHONE_ATTEMPT_KEY_PREFIX + username, CODE_TTL_MINUTES, TimeUnit.MINUTES);
        return value;
    }

    public void clearPhoneCode(String username) {
        redisTemplate.delete(PHONE_VERIFY_KEY_PREFIX + username);
        redisTemplate.delete(PHONE_ATTEMPT_KEY_PREFIX + username);
    }

    public boolean hasRegisterCooldown(String type, String value) {
        return StringUtils.hasText(redisTemplate.opsForValue().get(registerCooldownKey(type, value)));
    }

    public void saveRegisterCode(String type, String value, String code) {
        redisTemplate.opsForValue().set(registerCodeKey(type, value), code, CODE_TTL_MINUTES, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(registerCooldownKey(type, value), "1", RESEND_COOLDOWN_SECONDS, TimeUnit.SECONDS);
    }

    public String readRegisterCode(String type, String value) {
        return redisTemplate.opsForValue().get(registerCodeKey(type, value));
    }

    public Long incrementRegisterAttempts(String type, String value) {
        Long attempts = redisTemplate.opsForValue().increment(registerAttemptKey(type, value));
        redisTemplate.expire(registerAttemptKey(type, value), CODE_TTL_MINUTES, TimeUnit.MINUTES);
        return attempts;
    }

    public void clearRegisterCode(String type, String value) {
        redisTemplate.delete(registerCodeKey(type, value));
        redisTemplate.delete(registerAttemptKey(type, value));
    }

    public String issueVerifiedToken(String type, String value) {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(registerVerifiedTokenKey(type, token), value, VERIFY_TOKEN_TTL_MINUTES, TimeUnit.MINUTES);
        return token;
    }

    public String readVerifiedToken(String type, String token) {
        return redisTemplate.opsForValue().get(registerVerifiedTokenKey(type, token));
    }

    public void consumeVerifiedToken(String type, String token) {
        redisTemplate.delete(registerVerifiedTokenKey(type, token));
    }

    private String registerCodeKey(String type, String value) {
        return REGISTER_KEY_PREFIX + type + ":code:" + value;
    }

    private String registerCooldownKey(String type, String value) {
        return REGISTER_KEY_PREFIX + type + ":cooldown:" + value;
    }

    private String registerAttemptKey(String type, String value) {
        return REGISTER_KEY_PREFIX + type + ":attempt:" + value;
    }

    private String registerVerifiedTokenKey(String type, String token) {
        return REGISTER_KEY_PREFIX + type + ":verified:" + token;
    }
}
