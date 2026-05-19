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
public class AuthUserMenuPermissionId implements Serializable {

    @Column(name = "USER_ID", nullable = false, length = 20)
    private String userId;

    @Column(name = "MENU_ID", nullable = false)
    private Integer menuId;
}
