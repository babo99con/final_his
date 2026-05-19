package app.auth.permission.repository;

import app.auth.permission.entity.AuthUserMenuPermission;
import app.auth.permission.entity.AuthUserMenuPermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserMenuPermissionRepository extends JpaRepository<AuthUserMenuPermission, AuthUserMenuPermissionId> {

    void deleteByIdUserId(String userId);
}
