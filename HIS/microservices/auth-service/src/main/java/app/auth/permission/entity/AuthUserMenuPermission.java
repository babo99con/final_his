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
@Table(name = "AUTH_USER_MENU_PERMISSION", schema = "HOSPITAL")
public class AuthUserMenuPermission {

    @EmbeddedId
    private AuthUserMenuPermissionId id;

    @Column(name = "CAN_VIEW", length = 1)
    private String canView;

    @Column(name = "CAN_CREATE", length = 1)
    private String canCreate;

    @Column(name = "CAN_UPDATE", length = 1)
    private String canUpdate;

    @Column(name = "CAN_DELETE", length = 1)
    private String canDelete;
}
