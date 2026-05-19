package app.auth.oauth.repository;

import app.auth.common.entity.AuthAccount;
import app.auth.oauth.entity.SocialVerification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Repository
public class OAuthAccountRepository {

    private static final String SOCIAL_REGISTER_KEY_PREFIX = "auth:social:register:";
    private static final long SOCIAL_TOKEN_TTL_MINUTES = 10;

    private final StringRedisTemplate redisTemplate;
    private final SecureRandom secureRandom = new SecureRandom();

    public OAuthAccountRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String issueSocialVerificationToken(SocialVerification verification) {
        String token = randomToken();
        String value = buildStoredValue(verification);
        redisTemplate.opsForValue().set(SOCIAL_REGISTER_KEY_PREFIX + token, value, SOCIAL_TOKEN_TTL_MINUTES, TimeUnit.MINUTES);
        return token;
    }

    public SocialVerification readSocialVerification(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        String value = redisTemplate.opsForValue().get(SOCIAL_REGISTER_KEY_PREFIX + token.trim());
        if (!StringUtils.hasText(value)) {
            return null;
        }

        String[] parts = value.split("\\|", 4);
        String provider = readPart(parts, 0);
        String providerId = readPart(parts, 1);
        String name = readPart(parts, 2);
        String email = readPart(parts, 3);

        return new SocialVerification(provider, providerId, name, email);
    }

    public void consumeSocialVerification(String token) {
        if (StringUtils.hasText(token)) {
            redisTemplate.delete(SOCIAL_REGISTER_KEY_PREFIX + token.trim());
        }
    }

    public AuthAccount findActiveOAuthAccount(String provider, String providerId) {
        // This is a simplified implementation - in a real app, you would query your OAuth account table
        // For now, we'll return null to force registration of new OAuth accounts
        return null;
    }

    public AuthAccount registerOAuthAccount(String provider, String providerId, String email, String fullName) {
        // This is a simplified implementation - in a real app, you would save to your OAuth account table
        // For now, we'll delegate to the regular account registration
        AuthAccount account = new AuthAccount();
        account.setUsername(providerId); // Using providerId as username for simplicity
        account.setFullName(fullName);
        account.setRole("USER"); // Default role
        account.setStatus("ACTIVE");
        // Password hash will be set during actual registration flow
        return account;
    }

    private String randomToken() {
        byte[] bytes = new byte[24];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String buildStoredValue(SocialVerification verification) {
        String provider = safe(verification.provider());
        String providerId = safe(verification.providerId());
        String name = safe(verification.name());
        String email = safe(verification.email());

        return provider + "|" + providerId + "|" + name + "|" + email;
    }

    private String readPart(String[] parts, int index) {
        if (parts.length <= index) {
            return null;
        }

        return unsafe(parts[index]);
    }

    private String safe(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("|", "");
    }

    private String unsafe(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        return value;
    }
}
