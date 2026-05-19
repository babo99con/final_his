package app.auth.register.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Set;

@Component
public class RegisterValidator {

    private static final Set<String> ALLOWED_ROLE_CODES = Set.of(
            "ADMIN",
            "DOCTOR",
            "NURSE",
            "RECEPTION",
            "STAFF",
            "RADIOLOGY_TECH",
            "CLINICAL_LAB_TECH",
            "PATHOLOGY_COORDINATOR",
            "ENDOSCOPY_COORDINATOR",
            "PHYSIOLOGY_TEST_COORDINATOR"
    );

    public void validateUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("AUTH_USERNAME_REQUIRED");
        }
        if (!username.trim().matches("^[a-zA-Z0-9._-]{4,30}$")) {
            throw new IllegalArgumentException("AUTH_USERNAME_INVALID");
        }
    }

    public void validatePassword(String password) {
        if (!StringUtils.hasText(password) || password.trim().length() < 8) {
            throw new IllegalArgumentException("AUTH_PASSWORD_TOO_SHORT");
        }
    }

    public void validateRole(String roleCode) {
        if (!StringUtils.hasText(roleCode)) {
            throw new IllegalArgumentException("AUTH_ROLE_REQUIRED");
        }
        if (!ALLOWED_ROLE_CODES.contains(roleCode.trim().toUpperCase())) {
            throw new IllegalArgumentException("AUTH_ROLE_INVALID");
        }
    }
}
