package app.auth.permission.repository;

import app.auth.permission.dto.PermissionRoleResponse;
import app.auth.permission.entity.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthRoleRepository extends JpaRepository<AuthRole, String> {

    @Query(value = """
        SELECT DISTINCT
            r.ROLE_CODE AS roleCode,
            r.ROLE_NAME AS roleName
        FROM CMH.AUTH_ROLE r
        JOIN CMH.AUTH_USER u ON u.ROLE_CODE = r.ROLE_CODE
        WHERE r.IS_ACTIVE = 'Y'
        ORDER BY r.ROLE_CODE
        """, nativeQuery = true)
    List<RoleSummaryView> findRoleSummariesInUse();

    default List<PermissionRoleResponse> findRolesInUse() {
        return findRoleSummariesInUse().stream()
                .map(view -> new PermissionRoleResponse(view.getRoleCode(), view.getRoleName()))
                .toList();
    }

    interface RoleSummaryView {
        String getRoleCode();
        String getRoleName();
    }
}
