package app.auth.permission.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "AUTH_ROLE_MENU_PERMISSION", schema = "CMH")
public class AuthRoleMenuPermission {

    @EmbeddedId
    private AuthRoleMenuPermissionId id;

    @Column(name = "CAN_VIEW", nullable = false, length = 1)
    private String canView;

    @Column(name = "CAN_CREATE", nullable = false, length = 1)
    private String canCreate;

    @Column(name = "CAN_UPDATE", nullable = false, length = 1)
    private String canUpdate;

    @Column(name = "CAN_DELETE", nullable = false, length = 1)
    private String canDelete;
}
