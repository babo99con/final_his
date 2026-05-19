package app.auth.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUserProfileInfo {

    private String fullName;
    private String status;
    private String departmentId;
    private String departmentName;
}
