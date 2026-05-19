package app.auth.me.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MeValidator {

    public void validateAuthenticatedUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("AUTH_UNAUTHORIZED");
        }
    }
}
