package app.auth.permission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "권한을 설정할 사용자 정보입니다.")
public class PermissionUserResponse {

    @Schema(description = "사용자 번호입니다.")
    private String userId;

    @Schema(description = "로그인 아이디입니다.")
    private String username;

    @Schema(description = "사용자 이름입니다.")
    private String fullName;

    @Schema(description = "사용자의 역할 코드입니다.")
    private String roleCode;

    @Schema(description = "사용자 상태입니다. 예: 재직, 퇴직")
    private String status;

    @Schema(description = "소속 부서 이름입니다.")
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
