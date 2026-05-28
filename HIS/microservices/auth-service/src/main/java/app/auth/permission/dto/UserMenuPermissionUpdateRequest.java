package app.auth.permission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "사용자별 메뉴 권한을 저장할 때 보내는 목록입니다.")
public class UserMenuPermissionUpdateRequest {

    @Schema(description = "바꿀 메뉴 권한 목록입니다.")
    private List<UserMenuPermissionItemRequest> permissions = new ArrayList<>();

    public UserMenuPermissionUpdateRequest() {
    }

    public List<UserMenuPermissionItemRequest> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<UserMenuPermissionItemRequest> permissions) {
        this.permissions = permissions;
    }
}
