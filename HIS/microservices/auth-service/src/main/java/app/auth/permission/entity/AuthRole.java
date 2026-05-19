package app.auth.permission.entity;

import app.auth.common.entity.AuditableEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "AUTH_ROLE", schema = "CMH")
public class AuthRole extends AuditableEntity {

    @Id
    @Column(name = "ROLE_CODE", nullable = false, length = 50)
    private String roleCode;

    @Column(name = "ROLE_NAME", nullable = false, length = 100)
    private String roleName;

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private String isActive;
}
