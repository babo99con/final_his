package app.auth.permission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "역할별 메뉴 권한을 저장할 때 보내는 목록입니다.")
public class RoleMenuPermissionUpdateRequest {

    @Schema(description = "바꿀 메뉴 권한 목록입니다.")
    private List<RoleMenuPermissionItemRequest> permissions = new ArrayList<>();

    public RoleMenuPermissionUpdateRequest() {
    }

    public List<RoleMenuPermissionItemRequest> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<RoleMenuPermissionItemRequest> permissions) {
        this.permissions = permissions;
    }
}
