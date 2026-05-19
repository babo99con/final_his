package app.auth.session.repository;

import app.auth.session.entity.AuthSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<AuthSession, String> {

    boolean existsBySessionIdAndUserIdAndAccessTokenJtiAndIsRevokedAndExpiresAtAfter(
            String sessionId,
            String userId,
            String accessTokenJti,
            String isRevoked,
            LocalDateTime currentTime
    );

    boolean existsBySessionIdAndUserIdAndRefreshTokenJtiAndIsRevokedAndExpiresAtAfter(
            String sessionId,
            String userId,
            String refreshTokenJti,
            String isRevoked,
            LocalDateTime currentTime
    );

    List<AuthSession> findByUserIdAndIsRevoked(String userId, String isRevoked);

    Optional<AuthSession> findBySessionIdAndUserIdAndIsRevoked(String sessionId, String userId, String isRevoked);
}
