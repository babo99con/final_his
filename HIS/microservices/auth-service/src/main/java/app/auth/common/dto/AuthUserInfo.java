package app.auth.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "현재 로그인한 사용자 정보입니다.")
public class AuthUserInfo {

    @Schema(description = "사용자 번호입니다.")
    private String userId;

    @Schema(description = "로그인 아이디입니다.")
    private String username;

    @Schema(description = "사용자 이름입니다.")
    private String fullName;

    @Schema(description = "사용자의 역할입니다.")
    private String role;

    @Schema(description = "소속 부서 번호입니다.")
    private String departmentId;

    @Schema(description = "소속 부서 이름입니다.")
    private String departmentName;
}
