package app.auth.verification.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Component
public class VerificationValidator {

    public String normalizeEmail(String rawEmail) {
        String email = rawEmail == null ? "" : rawEmail.trim().toLowerCase(Locale.ROOT);
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("AUTH_EMAIL_INVALID");
        }
        return email;
    }

    public String normalizePhone(String rawPhone) {
        String phone = rawPhone == null ? "" : rawPhone.trim().replaceAll("[^0-9]", "");
        if (!phone.matches("^01\\d{8,9}$")) {
            throw new IllegalArgumentException("AUTH_PHONE_INVALID");
        }
        return phone;
    }

    public String normalizeCode(String rawCode) {
        String code = rawCode == null ? "" : rawCode.trim();
        if (!code.matches("^\\d{6}$")) {
            throw new IllegalArgumentException("AUTH_CODE_INVALID");
        }
        return code;
    }

    public String requireUsername(String username) {
        String value = username == null ? "" : username.trim().toLowerCase(Locale.ROOT);
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("AUTH_UNAUTHORIZED");
        }
        return value;
    }
}
