package app.auth.permission.repository;

import app.auth.permission.entity.AuthRoleMenuPermission;
import app.auth.permission.entity.AuthRoleMenuPermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRoleMenuPermissionRepository extends JpaRepository<AuthRoleMenuPermission, AuthRoleMenuPermissionId> {

    void deleteByIdRoleCode(String roleCode);
}
