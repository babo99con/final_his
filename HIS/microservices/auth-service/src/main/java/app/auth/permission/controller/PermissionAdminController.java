package app.auth.permission.controller;

import app.auth.permission.dto.PermissionRoleResponse;
import app.auth.permission.dto.PermissionUserResponse;
import app.auth.permission.dto.RoleMenuPermissionResponse;
import app.auth.permission.dto.RoleMenuPermissionUpdateRequest;
import app.auth.permission.dto.UserMenuPermissionResponse;
import app.auth.permission.dto.UserMenuPermissionUpdateRequest;
import app.auth.permission.service.PermissionAdminService;
import com.hms.util.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/permissions")
public class PermissionAdminController {

    private final PermissionAdminService permissionAdminService;

    public PermissionAdminController(PermissionAdminService permissionAdminService) {
        this.permissionAdminService = permissionAdminService;
    }

    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<List<PermissionRoleResponse>>> getRoles() {
        List<PermissionRoleResponse> roles = permissionAdminService.getRoles();
        return ResponseEntity.ok(ApiResponse.ok(roles));
    }

    @GetMapping("/roles/{roleCode}/menus")
    public ResponseEntity<ApiResponse<List<RoleMenuPermissionResponse>>> getRoleMenus(@PathVariable String roleCode) {
        try {
            List<RoleMenuPermissionResponse> menus = permissionAdminService.getRoleMenuPermissions(roleCode);
            return ResponseEntity.ok(ApiResponse.ok(menus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/roles/{roleCode}/menus")
    public ResponseEntity<ApiResponse<Void>> updateRoleMenus(@PathVariable String roleCode,
                                                             @RequestBody RoleMenuPermissionUpdateRequest request) {
        try {
            permissionAdminService.updateRoleMenuPermissions(roleCode, request);
            return ResponseEntity.ok(ApiResponse.ok("Role menu permissions saved."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<PermissionUserResponse>>> searchUsers(@RequestParam(required = false) String keyword) {
        List<PermissionUserResponse> users = permissionAdminService.searchUsers(keyword);
        return ResponseEntity.ok(ApiResponse.ok(users));
    }

    @GetMapping("/users/{userId}/menus")
    public ResponseEntity<ApiResponse<List<UserMenuPermissionResponse>>> getUserMenus(@PathVariable String userId) {
        try {
            List<UserMenuPermissionResponse> menus = permissionAdminService.getUserMenuPermissions(userId);
            return ResponseEntity.ok(ApiResponse.ok(menus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/users/{userId}/menus")
    public ResponseEntity<ApiResponse<Void>> updateUserMenus(@PathVariable String userId,
                                                             @RequestBody UserMenuPermissionUpdateRequest request) {
        try {
            permissionAdminService.updateUserMenuPermissions(userId, request);
            return ResponseEntity.ok(ApiResponse.ok("User menu override saved."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
