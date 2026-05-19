package app.auth.me.service;

import app.auth.common.PasswordHashUtil;
import app.auth.common.dto.AuthUserInfo;
import app.auth.common.dto.AuthUserProfileInfo;
import app.auth.common.entity.AuthAccount;
import app.auth.common.repository.AuthUserProfileRepository;
import app.auth.me.dto.ChangePasswordRequest;
import app.auth.me.mapper.MeMapper;
import app.auth.me.validator.MeValidator;
import app.auth.register.repository.RegisterAccountRepository;
import app.auth.register.validator.RegisterValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@AllArgsConstructor
public class MeServiceImpl implements MeService {

    private final RegisterAccountRepository registerAccountRepository;
    private final AuthUserProfileRepository authUserProfileRepository;
    private final MeMapper meMapper;
    private final MeValidator meValidator;
    private final RegisterValidator registerValidator;

    @Override
    public AuthUserInfo getCurrentUserInfo(String username) {
        meValidator.validateAuthenticatedUsername(username);
        String normalizedUsername = normalizeUsername(username);
        AuthAccount account = registerAccountRepository.findByUsernameIgnoreCase(normalizedUsername).orElse(null);

        if (account == null) {
            return null;
        }

        AuthUserProfileInfo profileInfo = authUserProfileRepository.readProfileInfo(account.getId());
        return meMapper.toUserInfo(account, profileInfo);
    }

    @Override
    public void changeMyPassword(String username, ChangePasswordRequest request) {
        meValidator.validateAuthenticatedUsername(username);
        validateChangePasswordRequest(request);

        String normalizedUsername = normalizeUsername(username);
        AuthAccount account = registerAccountRepository.findByUsernameIgnoreCase(normalizedUsername)
                .orElseThrow(() -> new IllegalArgumentException("AUTH_UNAUTHORIZED"));

        if (!PasswordHashUtil.matches(request.getCurrentPassword(), account.getPasswordHash())) {
            throw new IllegalArgumentException("AUTH_CURRENT_PASSWORD_INVALID");
        }

        registerValidator.validatePassword(request.getNewPassword());

        if (PasswordHashUtil.matches(request.getNewPassword(), account.getPasswordHash())) {
            throw new IllegalArgumentException("AUTH_PASSWORD_SAME_AS_CURRENT");
        }

        account.setPasswordHash(PasswordHashUtil.hashNew(request.getNewPassword().trim()));
        registerAccountRepository.save(account);
    }

    private String normalizeUsername(String username) {
        if (username == null) {
            return "";
        }

        return username.trim().toLowerCase();
    }

    private void validateChangePasswordRequest(ChangePasswordRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("AUTH_PASSWORD_CHANGE_REQUEST_REQUIRED");
        }

        if (!StringUtils.hasText(request.getCurrentPassword())) {
            throw new IllegalArgumentException("AUTH_CURRENT_PASSWORD_REQUIRED");
        }

        if (!StringUtils.hasText(request.getNewPassword())) {
            throw new IllegalArgumentException("AUTH_NEW_PASSWORD_REQUIRED");
        }
    }
}
