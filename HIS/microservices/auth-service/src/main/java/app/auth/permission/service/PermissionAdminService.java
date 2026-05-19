package app.auth.permission.service;

import app.auth.permission.dto.PermissionRoleResponse;
import app.auth.permission.dto.PermissionUserResponse;
import app.auth.permission.dto.RoleMenuPermissionResponse;
import app.auth.permission.dto.RoleMenuPermissionUpdateRequest;
import app.auth.permission.dto.UserMenuPermissionResponse;
import app.auth.permission.dto.UserMenuPermissionUpdateRequest;

import java.util.List;

public interface PermissionAdminService {

    List<PermissionRoleResponse> getRoles();

    List<PermissionUserResponse> searchUsers(String keyword);

    List<RoleMenuPermissionResponse> getRoleMenuPermissions(String roleCode);

    void updateRoleMenuPermissions(String roleCode, RoleMenuPermissionUpdateRequest request);

    List<UserMenuPermissionResponse> getUserMenuPermissions(String userId);

    void updateUserMenuPermissions(String userId, UserMenuPermissionUpdateRequest request);
}
