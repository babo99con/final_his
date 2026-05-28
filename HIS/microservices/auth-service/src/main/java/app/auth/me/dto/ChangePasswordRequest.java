package app.auth.me.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "비밀번호를 바꿀 때 보내는 정보입니다.")
public class ChangePasswordRequest {

    @Schema(description = "지금 사용 중인 비밀번호입니다.")
    private String currentPassword;

    @Schema(description = "새로 사용할 비밀번호입니다.")
    private String newPassword;
}
