package app.auth.permission.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class AuthRoleMenuPermissionId implements Serializable {

    @Column(name = "ROLE_CODE", nullable = false, length = 50)
    private String roleCode;

    @Column(name = "MENU_ID", nullable = false)
    private Integer menuId;
}
