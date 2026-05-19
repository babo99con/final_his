package app.auth.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUserInfo {

    private String userId;
    private String username;
    private String fullName;
    private String role;
    private String departmentId;
    private String departmentName;
}
