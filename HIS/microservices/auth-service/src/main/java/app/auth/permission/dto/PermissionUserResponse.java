package app.auth.permission.dto;

public class PermissionUserResponse {

    private String userId;
    private String username;
    private String fullName;
    private String roleCode;
    private String status;
    private String departmentName;

    public PermissionUserResponse() {
    }

    public PermissionUserResponse(String userId,
                                  String username,
                                  String fullName,
                                  String roleCode,
                                  String status,
                                  String departmentName) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.roleCode = roleCode;
        this.status = status;
        this.departmentName = departmentName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStaffId() {
        return userId;
    }

    public void setStaffId(String staffId) {
        this.userId = staffId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
