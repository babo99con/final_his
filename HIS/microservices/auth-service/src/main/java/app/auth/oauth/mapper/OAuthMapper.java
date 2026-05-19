package app.auth.oauth.mapper;

import app.auth.oauth.entity.OAuthProfile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
public class OAuthMapper {

    public OAuthProfile toProfile(String provider, Map<String, Object> attributes) {
        String normalized = provider == null ? "" : provider.trim().toLowerCase();

        if ("google".equals(normalized)) {
            return new OAuthProfile("google", toString(attributes.get("sub")), toString(attributes.get("email")), toString(attributes.get("name")));
        }

        if ("naver".equals(normalized)) {
            Object response = attributes.get("response");
            if (response instanceof Map<?, ?> map) {
                return new OAuthProfile("naver", toString(map.get("id")), toString(map.get("email")), toString(map.get("name")));
            }
        }

        if ("kakao".equals(normalized)) {
            String providerId = toString(attributes.get("id"));
            String email = null;
            String name = null;

            Object account = attributes.get("kakao_account");
            if (account instanceof Map<?, ?> accountMap) {
                email = toString(accountMap.get("email"));
                Object profile = accountMap.get("profile");
                if (profile instanceof Map<?, ?> profileMap) {
                    name = toString(profileMap.get("nickname"));
                }
            }
            return new OAuthProfile("kakao", providerId, email, name);
        }

        throw new IllegalArgumentException("AUTH_OAUTH_PROVIDER_UNSUPPORTED");
    }

    private String toString(Object value) {
        if (value == null) {
            return null;
        }
        String text = String.valueOf(value).trim();
        return StringUtils.hasText(text) ? text : null;
    }
}
