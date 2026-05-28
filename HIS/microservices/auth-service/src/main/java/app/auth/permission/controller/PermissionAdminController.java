package app.auth.permission.controller;

import app.auth.permission.dto.PermissionRoleResponse;
import app.auth.permission.dto.PermissionUserResponse;
import app.auth.permission.dto.RoleMenuPermissionResponse;
import app.auth.permission.dto.RoleMenuPermissionUpdateRequest;
import app.auth.permission.dto.UserMenuPermissionResponse;
import app.auth.permission.dto.UserMenuPermissionUpdateRequest;
import app.auth.permission.service.PermissionAdminService;
import com.hms.util.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "권한 관리", description = "관리자가 역할별, 사용자별 메뉴 사용 권한을 확인하고 바꾸는 API입니다.")
public class PermissionAdminController {

    private final PermissionAdminService permissionAdminService;

    public PermissionAdminController(PermissionAdminService permissionAdminService) {
        this.permissionAdminService = permissionAdminService;
    }

    @GetMapping("/roles")
    @Operation(
            summary = "역할 목록 조회",
            description = "시스템에 등록된 역할 목록을 가져옵니다. 예를 들면 관리자, 의사, 간호사 같은 구분입니다."
    )
    public ResponseEntity<ApiResponse<List<PermissionRoleResponse>>> getRoles() {
        List<PermissionRoleResponse> roles = permissionAdminService.getRoles();
        ApiResponse<List<PermissionRoleResponse>> responseBody = ApiResponse.ok(roles);

        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/roles/{roleCode}/menus")
    @Operation(
            summary = "역할이 사용할 수 있는 메뉴 조회",
            description = "특정 역할이 어떤 메뉴를 볼 수 있고, 만들기/수정/삭제를 할 수 있는지 확인합니다."
    )
    public ResponseEntity<ApiResponse<List<RoleMenuPermissionResponse>>> getRoleMenus(
            @Parameter(description = "권한을 확인할 역할 코드입니다. 예: ADMIN, DOCTOR")
            @PathVariable String roleCode) {
        try {
            List<RoleMenuPermissionResponse> menus = permissionAdminService.getRoleMenuPermissions(roleCode);
            ApiResponse<List<RoleMenuPermissionResponse>> responseBody = ApiResponse.ok(menus);

            return ResponseEntity.ok(responseBody);
        } catch (IllegalArgumentException e) {
            ApiResponse<List<RoleMenuPermissionResponse>> errorBody = ApiResponse.error(e.getMessage());

            return ResponseEntity.badRequest().body(errorBody);
        }
    }

    @PutMapping("/roles/{roleCode}/menus")
    @Operation(
            summary = "역할 메뉴 권한 저장",
            description = "특정 역할이 메뉴별로 보기, 만들기, 수정, 삭제를 할 수 있는지 저장합니다."
    )
    public ResponseEntity<ApiResponse<Void>> updateRoleMenus(
            @Parameter(description = "권한을 바꿀 역할 코드입니다. 예: ADMIN, DOCTOR")
            @PathVariable String roleCode,
            @RequestBody RoleMenuPermissionUpdateRequest request) {
        try {
            permissionAdminService.updateRoleMenuPermissions(roleCode, request);
            ApiResponse<Void> responseBody = ApiResponse.ok("Role menu permissions saved.");

            return ResponseEntity.ok(responseBody);
        } catch (IllegalArgumentException e) {
            ApiResponse<Void> errorBody = ApiResponse.error(e.getMessage());

            return ResponseEntity.badRequest().body(errorBody);
        }
    }

    @GetMapping("/users")
    @Operation(
            summary = "사용자 검색",
            description = "이름, 아이디 같은 검색어로 권한을 바꿀 사용자를 찾습니다. 검색어를 비우면 전체 사용자를 조회합니다."
    )
    public ResponseEntity<ApiResponse<List<PermissionUserResponse>>> searchUsers(
            @Parameter(description = "찾고 싶은 사용자 이름이나 아이디입니다. 비워도 됩니다.")
            @RequestParam(required = false) String keyword) {
        List<PermissionUserResponse> users = permissionAdminService.searchUsers(keyword);
        ApiResponse<List<PermissionUserResponse>> responseBody = ApiResponse.ok(users);

        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/users/{userId}/menus")
    @Operation(
            summary = "사용자 한 명의 메뉴 권한 조회",
            description = "사용자 한 명이 실제로 어떤 메뉴를 사용할 수 있는지 확인합니다. 역할 권한과 개인별 예외 권한을 함께 보여줍니다."
    )
    public ResponseEntity<ApiResponse<List<UserMenuPermissionResponse>>> getUserMenus(
            @Parameter(description = "메뉴 권한을 확인할 사용자 번호입니다.")
            @PathVariable String userId) {
        try {
            List<UserMenuPermissionResponse> menus = permissionAdminService.getUserMenuPermissions(userId);
            ApiResponse<List<UserMenuPermissionResponse>> responseBody = ApiResponse.ok(menus);

            return ResponseEntity.ok(responseBody);
        } catch (IllegalArgumentException e) {
            ApiResponse<List<UserMenuPermissionResponse>> errorBody = ApiResponse.error(e.getMessage());

            return ResponseEntity.badRequest().body(errorBody);
        }
    }

    @PutMapping("/users/{userId}/menus")
    @Operation(
            summary = "사용자 한 명의 메뉴 권한 저장",
            description = "역할 권한과 다르게, 특정 사용자에게만 메뉴 권한을 따로 허용하거나 막습니다."
    )
    public ResponseEntity<ApiResponse<Void>> updateUserMenus(
            @Parameter(description = "메뉴 권한을 바꿀 사용자 번호입니다.")
            @PathVariable String userId,
            @RequestBody UserMenuPermissionUpdateRequest request) {
        try {
            permissionAdminService.updateUserMenuPermissions(userId, request);
            ApiResponse<Void> responseBody = ApiResponse.ok("User menu override saved.");

            return ResponseEntity.ok(responseBody);
        } catch (IllegalArgumentException e) {
            ApiResponse<Void> errorBody = ApiResponse.error(e.getMessage());

            return ResponseEntity.badRequest().body(errorBody);
        }
    }
}
