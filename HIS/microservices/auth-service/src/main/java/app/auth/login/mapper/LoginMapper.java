package app.auth.login.mapper;

import app.auth.common.dto.AuthUserProfileInfo;
import app.auth.common.entity.AuthAccount;
import app.auth.login.dto.LoginResponse;
import app.auth.login.dto.LoginUserDto;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {

    public LoginResponse toResponse(AuthAccount account,
                                    AuthUserProfileInfo profileInfo,
                                    String accessToken,
                                    long expiresIn,
                                    boolean passwordChangeRequired) {
        LoginUserDto user = new LoginUserDto(
                account.getId(),
                account.getUsername(),
                resolveFullName(account, profileInfo),
                account.getRole(),
                profileInfo == null ? null : profileInfo.getDepartmentId(),
                profileInfo == null ? null : profileInfo.getDepartmentName()
        );

        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setTokenType("Bearer");
        response.setExpiresIn(expiresIn);
        response.setUser(user);
        response.setPasswordChangeRequired(passwordChangeRequired);
        return response;
    }

    private String resolveFullName(AuthAccount account, AuthUserProfileInfo profileInfo) {
        if (profileInfo != null && hasText(profileInfo.getFullName())) {
            return profileInfo.getFullName();
        }

        if (account != null && hasText(account.getFullName())) {
            return account.getFullName();
        }

        return account == null ? null : account.getUsername();
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
