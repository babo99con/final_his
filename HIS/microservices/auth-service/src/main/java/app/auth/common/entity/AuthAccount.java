package app.auth.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "AUTH_USER", schema = "HOSPITAL")
public class AuthAccount {

    @Id
    @Column(name = "ID", nullable = false, length = 20)
    private String id;

    // DB schema uses LOGIN_ID instead of USERNAME.
    @Column(name = "LOGIN_ID", nullable = false, length = 50)
    private String username;

    @Transient
    private String fullName;

    @Column(name = "ROLE_CODE", nullable = false, length = 50)
    private String role;

    @Column(name = "PASSWORD_HASH", nullable = false, length = 100)
    private String passwordHash;

    // DB has ACCOUNT_STATUS; profile/status is still enriched from HOSPITAL.EMPLOYEE.
    @Column(name = "ACCOUNT_STATUS")
    private String status;

    public AuthAccount(String id,
                       String username,
                       String role,
                       String passwordHash) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.passwordHash = passwordHash;
    }
}
