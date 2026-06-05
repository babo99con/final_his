package app.auth.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "로그인할 때 보내는 아이디와 비밀번호입니다.")
public class LoginRequest {
    @Schema(description = "로그인 아이디입니다.", example = "26-6001")
    private String username;

    @Schema(description = "로그인 비밀번호입니다.", example = "1111")
    private String password;
}
