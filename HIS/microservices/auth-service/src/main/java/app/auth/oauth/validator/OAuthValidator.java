package app.auth.oauth.validator;

import org.springframework.stereotype.Component;

@Component
public class OAuthValidator {

    public String normalizeProvider(String provider) {
        String normalized = provider == null ? "" : provider.trim().toLowerCase();
        if (!("naver".equals(normalized) || "kakao".equals(normalized) || "google".equals(normalized))) {
            throw new IllegalArgumentException("AUTH_OAUTH_PROVIDER_UNSUPPORTED");
        }
        return normalized;
    }
}
