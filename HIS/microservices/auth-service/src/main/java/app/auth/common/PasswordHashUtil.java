package app.auth.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

/**
 * Auth 공통 비밀번호 해시 유틸.
 *
 * 신규/변경 비밀번호는 bcrypt로 생성하고,
 * 기존 데이터 호환을 위해 bcrypt와 sha256 hex 형식을 모두 검증한다.
 */
public final class PasswordHashUtil {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private PasswordHashUtil() {
    }

    public static String hashNew(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }

        if (isBcryptHash(encodedPassword)) {
            return ENCODER.matches(rawPassword, encodedPassword);
        }

        if (isSha256Hash(encodedPassword)) {
            return sha256(rawPassword).equalsIgnoreCase(encodedPassword);
        }

        return rawPassword.equals(encodedPassword);
    }

    public static boolean isBcryptHash(String encodedPassword) {
        return encodedPassword != null && encodedPassword.startsWith("$2");
    }

    public static boolean isSha256Hash(String encodedPassword) {
        return encodedPassword != null && encodedPassword.matches("^[a-fA-F0-9]{64}$");
    }

    private static String sha256(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to hash password", e);
        }
    }
}
