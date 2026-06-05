package app.auth.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 성공 후 받는 정보입니다.")
public class LoginResponse {

    @Schema(description = "로그인한 사용자 정보입니다.")
    private LoginUserDto user;

    @Schema(description = "true이면 처음 로그인했거나 비밀번호를 바꿔야 하는 상태입니다.")
    private boolean passwordChangeRequired;
}
