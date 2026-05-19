package app.auth.session.service;

import app.auth.common.entity.AuthAccount;
import app.auth.register.repository.RegisterAccountRepository;
import app.auth.session.entity.AuthSession;
import app.auth.session.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Service
public class SessionServiceImpl implements SessionService {

    private final RegisterAccountRepository registerAccountRepository;
    private final SessionRepository sessionRepository;

    @Value("${app.auth.cookie-max-age-seconds:43200}")
    private int cookieMaxAge;

    @Value("${app.auth.cookie-secure:false}")
    private boolean cookieSecure;

    @Value("${app.auth.refresh-cookie-max-age-seconds:1209600}")
    private int refreshCookieMaxAge;

    @Value("${app.auth.session.absolute-seconds:43200}")
    private long absoluteSeconds;

    public SessionServiceImpl(RegisterAccountRepository registerAccountRepository,
                              SessionRepository sessionRepository) {
        this.registerAccountRepository = registerAccountRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void addAuthCookie(HttpServletResponse response, String token) {
        response.addHeader("Set-Cookie", buildCookieHeader(ACCESS_TOKEN_COOKIE, token, cookieMaxAge, true));
    }

    @Override
    public void clearAuthCookie(HttpServletResponse response) {
        response.addHeader("Set-Cookie", buildCookieHeader(ACCESS_TOKEN_COOKIE, "", 0, true));
    }

    @Override
    public void addRefreshCookie(HttpServletResponse response, String token) {
        response.addHeader("Set-Cookie", buildCookieHeader(REFRESH_TOKEN_COOKIE, token, refreshCookieMaxAge, true));
    }

    @Override
    public void clearRefreshCookie(HttpServletResponse response) {
        response.addHeader("Set-Cookie", buildCookieHeader(REFRESH_TOKEN_COOKIE, "", 0, true));
    }

    @Override
    @Transactional
    public void startSession(String username, String sid, String accessTokenJti, String refreshTokenJti) {
        AuthAccount account = findAccount(username);
        if (account == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        AuthSession session = new AuthSession();
        session.setSessionId(sid);
        session.setUserId(account.getId());
        session.setAccessTokenJti(accessTokenJti);
        session.setRefreshTokenJti(refreshTokenJti);
        session.setLoginAt(now);
        session.setLastAccessAt(now);
        session.setExpiresAt(createExpiresAt(now));
        session.setIsRevoked("N");
        session.setRevokedAt(null);

        revokeActiveSessions(account.getId(), now);
        sessionRepository.save(session);
    }

    @Override
    @Transactional
    public boolean isAccessTokenAliveAndTouch(String username, String sid, String accessTokenJti) {
        AuthAccount account = findAccount(username);
        if (account == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        boolean alive = sessionRepository.existsBySessionIdAndUserIdAndAccessTokenJtiAndIsRevokedAndExpiresAtAfter(
                sid,
                account.getId(),
                accessTokenJti,
                "N",
                now
        );
        if (alive) {
            sessionRepository.findById(sid).ifPresent(session -> {
                session.setLastAccessAt(now);
                sessionRepository.save(session);
            });
        }

        return alive;
    }

    @Override
    public boolean isRefreshTokenAlive(String username, String sid, String refreshTokenJti) {
        AuthAccount account = findAccount(username);
        if (account == null) {
            return false;
        }

        return sessionRepository.existsBySessionIdAndUserIdAndRefreshTokenJtiAndIsRevokedAndExpiresAtAfter(
                sid,
                account.getId(),
                refreshTokenJti,
                "N",
                LocalDateTime.now()
        );
    }

    @Override
    @Transactional
    public void rotateSessionTokens(String username, String sid, String accessTokenJti, String refreshTokenJti) {
        AuthAccount account = findAccount(username);
        if (account == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        sessionRepository.findBySessionIdAndUserIdAndIsRevoked(sid, account.getId(), "N")
                .ifPresent(session -> {
                    session.setAccessTokenJti(accessTokenJti);
                    session.setRefreshTokenJti(refreshTokenJti);
                    session.setLastAccessAt(now);
                    session.setExpiresAt(createExpiresAt(now));
                    sessionRepository.save(session);
                });
    }

    @Override
    @Transactional
    public void invalidateSession(String username) {
        AuthAccount account = findAccount(username);
        if (account == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        revokeActiveSessions(account.getId(), now);
    }

    private String buildCookieHeader(String name, String token, int maxAge, boolean httpOnly) {
        String value = "";
        if (StringUtils.hasText(token)) {
            value = token;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(name).append("=").append(value);
        builder.append("; Path=/");
        builder.append("; Max-Age=").append(maxAge);
        builder.append("; SameSite=Lax");

        if (httpOnly) {
            builder.append("; HttpOnly");
        }

        if (cookieSecure) {
            builder.append("; Secure");
        }

        return builder.toString();
    }

    private String normalizeUsername(String username) {
        if (username == null) {
            return "";
        }

        return username.trim().toLowerCase();
    }

    private LocalDateTime createExpiresAt(LocalDateTime now) {
        return now.plusSeconds(absoluteSeconds);
    }

    private AuthAccount findAccount(String username) {
        String normalizedUsername = normalizeUsername(username);
        if (!StringUtils.hasText(normalizedUsername)) {
            return null;
        }

        return registerAccountRepository.findByUsernameIgnoreCase(normalizedUsername).orElse(null);
    }

    private void revokeActiveSessions(String userId, LocalDateTime now) {
        var activeSessions = sessionRepository.findByUserIdAndIsRevoked(userId, "N");
        for (AuthSession session : activeSessions) {
            session.setIsRevoked("Y");
            session.setRevokedAt(now);
        }
        sessionRepository.saveAll(activeSessions);
    }
}
