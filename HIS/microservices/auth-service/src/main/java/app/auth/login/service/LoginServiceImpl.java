package app.auth.login.service;

import app.auth.common.PasswordHashUtil;
import app.auth.common.dto.AuthUserProfileInfo;
import app.auth.common.entity.AuthAccount;
import app.auth.common.repository.AuthUserProfileRepository;
import app.auth.login.dto.LoginRequest;
import app.auth.login.dto.LoginResponse;
import app.auth.login.dto.LoginResult;
import app.auth.login.mapper.LoginMapper;
import app.auth.login.validator.LoginValidator;
import app.auth.register.repository.RegisterAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    private static final String INITIAL_PASSWORD = "1111";

    private final RegisterAccountRepository registerAccountRepository;
    private final AuthUserProfileRepository authUserProfileRepository;
    private final LoginMapper loginMapper;
    private final LoginValidator loginValidator;

    @Override
    public LoginResult login(LoginRequest request) {
        loginValidator.validate(request);

        String username = normalizeUsername(request.getUsername());
        AuthAccount account = registerAccountRepository.findByUsernameIgnoreCase(username).orElse(null);
        validateLoginCredentials(request, account);

        AuthUserProfileInfo profileInfo = readProfileInfo(account);
        validateAccountStatus(account, profileInfo);

        boolean passwordChangeRequired = PasswordHashUtil.matches(INITIAL_PASSWORD, account.getPasswordHash());
        LoginResponse response = loginMapper.toResponse(account, profileInfo, passwordChangeRequired);
        return new LoginResult(response);
    }

    private void validateLoginCredentials(LoginRequest request, AuthAccount account) {
        if (account == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        boolean passwordMatched = PasswordHashUtil.matches(request.getPassword(), account.getPasswordHash());
        if (!passwordMatched) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    private void validateAccountStatus(AuthAccount account, AuthUserProfileInfo profileInfo) {
        if (account == null) {
            throw new BadCredentialsException("AUTH_INVALID_CREDENTIALS");
        }

        String status = resolveStatus(profileInfo, account);
        if ("PENDING_APPROVAL".equals(status)) {
            throw new AccessDeniedException("AUTH_PENDING_APPROVAL");
        }

        if (!"ACTIVE".equals(status)) {
            throw new AccessDeniedException("AUTH_INACTIVE_ACCOUNT");
        }
    }

    private AuthUserProfileInfo readProfileInfo(AuthAccount account) {
        if (account == null) {
            return new AuthUserProfileInfo(null, null, null, null);
        }

        return authUserProfileRepository.readProfileInfo(account.getId());
    }

    private String resolveStatus(AuthUserProfileInfo profileInfo, AuthAccount account) {
        if (profileInfo != null && !isBlank(profileInfo.getStatus())) {
            return profileInfo.getStatus().trim().toUpperCase();
        }

        if (account != null && !isBlank(account.getStatus())) {
            return account.getStatus().trim().toUpperCase();
        }

        return "INACTIVE";
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String normalizeUsername(String username) {
        if (username == null) {
            return "";
        }

        return username.trim().toLowerCase();
    }
}
