package app.auth.permission.dto;

public class PermissionRoleResponse {

    private String roleCode;
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
