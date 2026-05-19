package app.auth.login.service;

import app.auth.common.PasswordHashUtil;
import app.auth.common.dto.AuthUserProfileInfo;
import app.auth.common.entity.AuthAccount;
import app.auth.common.repository.AuthUserProfileRepository;
import app.auth.login.dto.LoginRequest;
import app.auth.login.dto.LoginResponse;
import app.auth.login.dto.LoginResult;
import app.auth.login.mapper.LoginMapper;
import app.auth.login.validator.LoginValidator;
import app.auth.register.repository.RegisterAccountRepository;
import app.auth.session.service.SessionService;
import app.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    private static final String INITIAL_PASSWORD = "1111";

    private final JwtTokenProvider jwtTokenProvider;
    private final RegisterAccountRepository registerAccountRepository;
    private final AuthUserProfileRepository authUserProfileRepository;
    private final LoginMapper loginMapper;
    private final LoginValidator loginValidator;
    private final SessionService sessionService;

    @Override
    public LoginResult login(LoginRequest request) {
        loginValidator.validate(request);

        String username = normalizeUsername(request.getUsername());
        AuthAccount account = registerAccountRepository.findByUsernameIgnoreCase(username).orElse(null);
        validateLoginCredentials(request, account);

        AuthUserProfileInfo profileInfo = readProfileInfo(account);
        validateAccountStatus(account, profileInfo);

        boolean passwordChangeRequired = PasswordHashUtil.matches(INITIAL_PASSWORD, account.getPasswordHash());
        return issueLoginResult(account, profileInfo, passwordChangeRequired);
    }

    @Override
    public LoginResult refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new BadCredentialsException("AUTH_REFRESH_TOKEN_REQUIRED");
        }

        if (!jwtTokenProvider.isValid(refreshToken)) {
            throw new BadCredentialsException("AUTH_REFRESH_TOKEN_INVALID");
        }

        String token = refreshToken.trim();
        var claims = jwtTokenProvider.parseClaims(token);

        String username = claims.getSubject();
        String sid = claimString(claims, "sid");
        String refreshJti = claimString(claims, "jti");
        String tokenType = claimString(claims, "type");

        if (!"refresh".equalsIgnoreCase(tokenType)
                || isBlank(username)
                || isBlank(sid)
                || isBlank(refreshJti)) {
            throw new BadCredentialsException("AUTH_REFRESH_TOKEN_INVALID");
        }

        if (!sessionService.isRefreshTokenAlive(username, sid, refreshJti)) {
            throw new BadCredentialsException("AUTH_REFRESH_TOKEN_INVALID");
        }

        AuthAccount account = registerAccountRepository.findByUsernameIgnoreCase(username).orElse(null);
        AuthUserProfileInfo profileInfo = readProfileInfo(account);
        validateAccountStatus(account, profileInfo);

        boolean passwordChangeRequired = PasswordHashUtil.matches(INITIAL_PASSWORD, account.getPasswordHash());
        return rotateLoginResult(account, profileInfo, sid, passwordChangeRequired);
    }

    private void validateLoginCredentials(LoginRequest request, AuthAccount account) {
        if (account == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        boolean passwordMatched = PasswordHashUtil.matches(request.getPassword(), account.getPasswordHash());
        if (!passwordMatched) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    private void validateAccountStatus(AuthAccount account, AuthUserProfileInfo profileInfo) {
        if (account == null) {
            throw new BadCredentialsException("AUTH_INVALID_CREDENTIALS");
        }

        String status = resolveStatus(profileInfo, account);
        if ("PENDING_APPROVAL".equals(status)) {
            throw new AccessDeniedException("AUTH_PENDING_APPROVAL");
        }

        if (!"ACTIVE".equals(status)) {
            throw new AccessDeniedException("AUTH_INACTIVE_ACCOUNT");
        }
    }

    private LoginResult issueLoginResult(AuthAccount account,
                                         AuthUserProfileInfo profileInfo,
                                         boolean passwordChangeRequired) {
        String sid = UUID.randomUUID().toString();
        return createLoginResult(account, profileInfo, sid, passwordChangeRequired, false);
    }

    private LoginResult rotateLoginResult(AuthAccount account,
                                          AuthUserProfileInfo profileInfo,
                                          String sid,
                                          boolean passwordChangeRequired) {
        return createLoginResult(account, profileInfo, sid, passwordChangeRequired, true);
    }

    private LoginResult createLoginResult(AuthAccount account,
                                          AuthUserProfileInfo profileInfo,
                                          String sid,
                                          boolean passwordChangeRequired,
                                          boolean rotateExistingSession) {
        String accessJti = UUID.randomUUID().toString();
        String refreshJti = UUID.randomUUID().toString();

        Map<String, Object> accessClaims = createAccessClaims(account, profileInfo, sid, accessJti);
        Map<String, Object> refreshClaims = createRefreshClaims(sid, refreshJti);

        String accessToken = jwtTokenProvider.createToken(account.getUsername(), accessClaims);
        String refreshToken = jwtTokenProvider.createRefreshToken(account.getUsername(), refreshClaims);

        if (rotateExistingSession) {
            sessionService.rotateSessionTokens(account.getUsername(), sid, accessJti, refreshJti);
        } else {
            sessionService.startSession(account.getUsername(), sid, accessJti, refreshJti);
        }

        LoginResponse response = loginMapper.toResponse(
                account,
                profileInfo,
                accessToken,
                jwtTokenProvider.getExpirationSeconds(),
                passwordChangeRequired
        );

        return new LoginResult(response, refreshToken);
    }

    private AuthUserProfileInfo readProfileInfo(AuthAccount account) {
        if (account == null) {
            return new AuthUserProfileInfo(null, null, null, null);
        }

        return authUserProfileRepository.readProfileInfo(account.getId());
    }

    private Map<String, Object> createAccessClaims(AuthAccount account,
                                                   AuthUserProfileInfo profileInfo,
                                                   String sid,
                                                   String accessJti) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", account.getRole());
        claims.put("userId", account.getId());
        claims.put("fullName", resolveFullName(profileInfo, account));
        claims.put("sid", sid);
        claims.put("jti", accessJti);
        claims.put("type", "access");
        return claims;
    }

    private Map<String, Object> createRefreshClaims(String sid, String refreshJti) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sid", sid);
        claims.put("jti", refreshJti);
        claims.put("type", "refresh");
        return claims;
    }

    private String claimString(Map<String, Object> claims, String key) {
        Object value = claims.get(key);
        return value == null ? null : String.valueOf(value);
    }

    private String resolveStatus(AuthUserProfileInfo profileInfo, AuthAccount account) {
        if (profileInfo != null && !isBlank(profileInfo.getStatus())) {
            return profileInfo.getStatus().trim().toUpperCase();
        }

        if (account != null && !isBlank(account.getStatus())) {
            return account.getStatus().trim().toUpperCase();
        }

        return "INACTIVE";
    }

    private String resolveFullName(AuthUserProfileInfo profileInfo, AuthAccount account) {
        if (profileInfo != null && !isBlank(profileInfo.getFullName())) {
            return profileInfo.getFullName();
        }

        if (account != null && !isBlank(account.getFullName())) {
            return account.getFullName();
        }

        return account == null ? null : account.getUsername();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String normalizeUsername(String username) {
        if (username == null) {
            return "";
        }

        return username.trim().toLowerCase();
    }
}
