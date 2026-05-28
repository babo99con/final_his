package app.auth.session.service;

import app.auth.common.entity.AuthAccount;
import app.auth.register.repository.RegisterAccountRepository;
import app.auth.session.entity.AuthSession;
import app.auth.session.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class SessionServiceImpl implements SessionService {

    private final RegisterAccountRepository registerAccountRepository;
    private final SessionRepository sessionRepository;

    @Value("${app.auth.session.absolute-seconds:43200}")
    private long absoluteSeconds;

    public SessionServiceImpl(RegisterAccountRepository registerAccountRepository,
                              SessionRepository sessionRepository) {
        this.registerAccountRepository = registerAccountRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional
    public void startSession(String username, String sid) {
        AuthAccount account = findAccount(username);
        if (account == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        AuthSession session = new AuthSession();
        session.setSessionId(sid);
        session.setUserId(account.getId());
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
    public boolean isSessionAliveAndTouch(String username, String sid) {
        AuthAccount account = findAccount(username);
        if (account == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        boolean alive = sessionRepository.existsBySessionIdAndUserIdAndIsRevokedAndExpiresAtAfter(
                sid,
                account.getId(),
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
    @Transactional
    public void invalidateSession(String username, String sid) {
        AuthAccount account = findAccount(username);
        if (account == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        sessionRepository.findBySessionIdAndUserIdAndIsRevoked(sid, account.getId(), "N")
                .ifPresent(session -> {
                    session.setLastAccessAt(now);
                    session.setIsRevoked("Y");
                    session.setRevokedAt(now);
                    sessionRepository.save(session);
                });
    }

    @Override
    @Transactional
    public void invalidateUserSessions(String username) {
        AuthAccount account = findAccount(username);
        if (account == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        revokeActiveSessions(account.getId(), now);
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
