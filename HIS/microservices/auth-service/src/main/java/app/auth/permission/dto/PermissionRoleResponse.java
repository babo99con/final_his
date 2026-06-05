package app.auth.permission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "권한을 묶는 역할 정보입니다.")
public class PermissionRoleResponse {

    @Schema(description = "역할 코드입니다. 예: ADMIN, DOCTOR")
    private String roleCode;

    @Schema(description = "사람이 읽기 쉬운 역할 이름입니다.")
    private String roleName;

    public PermissionRoleResponse() {
    }

    public PermissionRoleResponse(String roleCode, String roleName) {
        this.roleCode = roleCode;
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
