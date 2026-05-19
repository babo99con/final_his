package app.auth.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUserSearchInfo {

    private String userId;
    private String username;
    private String fullName;
    private String roleCode;
    private String status;
    private String departmentName;
}
