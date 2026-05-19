package app.auth.me.mapper;

import app.auth.common.dto.AuthUserInfo;
import app.auth.common.dto.AuthUserProfileInfo;
import app.auth.common.entity.AuthAccount;
import org.springframework.stereotype.Component;

@Component
public class MeMapper {

    public AuthUserInfo toUserInfo(AuthAccount account, AuthUserProfileInfo profileInfo) {
        return new AuthUserInfo(
                account.getId(),
                account.getUsername(),
                resolveFullName(account, profileInfo),
                account.getRole(),
                profileInfo == null ? null : profileInfo.getDepartmentId(),
                profileInfo == null ? null : profileInfo.getDepartmentName()
        );
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
