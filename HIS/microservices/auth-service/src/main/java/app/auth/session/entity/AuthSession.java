package app.auth.session.entity;

import app.auth.common.entity.AuditableEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "AUTH_SESSION", schema = "HOSPITAL")
public class AuthSession extends AuditableEntity {

    @Id
    @Column(name = "SESSION_ID", nullable = false, length = 36)
    private String sessionId;

    @Column(name = "USER_ID", nullable = false, length = 20)
    private String userId;

    @Column(name = "LOGIN_AT", nullable = false)
    private LocalDateTime loginAt;

    @Column(name = "LAST_ACCESS_AT")
    private LocalDateTime lastAccessAt;

    @Column(name = "EXPIRES_AT", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "IS_REVOKED", nullable = false, length = 1)
    private String isRevoked;

    @Column(name = "REVOKED_AT")
    private LocalDateTime revokedAt;
}
