package app.auth.oauth.service;

import app.auth.common.dto.AuthUserProfileInfo;
import app.auth.common.entity.AuthAccount;
import app.auth.common.repository.AuthUserProfileRepository;
import app.auth.login.dto.LoginResponse;
import app.auth.login.dto.LoginResult;
import app.auth.login.mapper.LoginMapper;
import app.auth.oauth.entity.OAuthProfile;
import app.auth.oauth.repository.OAuthAccountRepository;
import app.auth.session.service.SessionService;
import app.security.JwtTokenProvider;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OAuthServiceImpl implements OAuthService {

    private final OAuthAccountRepository oAuthAccountRepository;
    private final AuthUserProfileRepository authUserProfileRepository;
    private final LoginMapper loginMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final SessionService sessionService;
    private final OAuthVerificationTokenService oAuthVerificationTokenService;

    public OAuthServiceImpl(OAuthAccountRepository oAuthAccountRepository,
                            AuthUserProfileRepository authUserProfileRepository,
                            LoginMapper loginMapper,
                            JwtTokenProvider jwtTokenProvider,
                            SessionService sessionService,
                            OAuthVerificationTokenService oAuthVerificationTokenService) {
        this.oAuthAccountRepository = oAuthAccountRepository;
        this.authUserProfileRepository = authUserProfileRepository;
        this.loginMapper = loginMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.sessionService = sessionService;
        this.oAuthVerificationTokenService = oAuthVerificationTokenService;
    }

    @Override
    public OAuthProfile mapProfile(String provider, Map<String, Object> attributes) {
        if (!StringUtils.hasText(provider)) {
            throw new BadCredentialsException("OAUTH_PROVIDER_INVALID");
        }

        String normalizedProvider = provider.trim().toLowerCase();

        if ("google".equals(normalizedProvider)) {
            return mapGoogle(attributes);
        }

        if ("naver".equals(normalizedProvider)) {
            return mapNaver(attributes);
        }

        if ("kakao".equals(normalizedProvider)) {
            return mapKakao(attributes);
        }

        throw new BadCredentialsException("OAUTH_PROVIDER_UNSUPPORTED");
    }

    @Override
    public String issueSocialVerificationToken(String provider,
                                               String providerId,
                                               String name,
                                               String email) {
        return oAuthVerificationTokenService.issue(provider, providerId, name, email);
    }

    @Override
    public LoginResult loginOrRegisterOAuth(String provider,
                                            String providerId,
                                            String email,
                                            String name) {
        String normalizedProvider = normalize(provider);
        String normalizedProviderId = normalize(providerId);

        if (!StringUtils.hasText(normalizedProvider) || !StringUtils.hasText(normalizedProviderId)) {
            throw new BadCredentialsException("OAUTH_PROFILE_INVALID");
        }

        AuthAccount account = oAuthAccountRepository.findActiveOAuthAccount(
                normalizedProvider,
                normalizedProviderId
        );

        if (account == null) {
            account = oAuthAccountRepository.registerOAuthAccount(
                    normalizedProvider,
                    normalizedProviderId,
                    normalizeNullable(email),
                    normalizeDisplayName(name)
            );
        }

        AuthUserProfileInfo profileInfo = readProfileInfo(account);
        validateAccountStatus(account, profileInfo);

        return issueLoginResult(account, profileInfo);
    }

    private LoginResult issueLoginResult(AuthAccount account, AuthUserProfileInfo profileInfo) {
        String sid = UUID.randomUUID().toString();
        String accessJti = UUID.randomUUID().toString();
        String refreshJti = UUID.randomUUID().toString();

        Map<String, Object> accessClaims = createAccessClaims(account, profileInfo, sid, accessJti);
        Map<String, Object> refreshClaims = createRefreshClaims(sid, refreshJti);

        String accessToken = jwtTokenProvider.createToken(account.getUsername(), accessClaims);
        String refreshToken = jwtTokenProvider.createRefreshToken(account.getUsername(), refreshClaims);

        sessionService.startSession(account.getUsername(), sid, accessJti, refreshJti);

        LoginResponse response = loginMapper.toResponse(
                account,
                profileInfo,
                accessToken,
                jwtTokenProvider.getExpirationSeconds(),
                false
        );

        return new LoginResult(response, refreshToken);
    }

    private AuthUserProfileInfo readProfileInfo(AuthAccount account) {
        if (account == null) {
            return new AuthUserProfileInfo(null, null, null, null);
        }

        if (!StringUtils.hasText(account.getId())) {
            return new AuthUserProfileInfo(account.getFullName(), account.getStatus(), null, null);
        }

        return authUserProfileRepository.readProfileInfo(account.getId());
    }

    private Map<String, Object> createAccessClaims(AuthAccount account,
                                                   AuthUserProfileInfo profileInfo,
                                                   String sid,
                                                   String accessJti) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", account.getRole());
        claims.put("userId", account.getId());
        claims.put("fullName", resolveFullName(profileInfo, account));
        claims.put("sid", sid);
        claims.put("jti", accessJti);
        claims.put("type", "access");
        return claims;
    }

    private Map<String, Object> createRefreshClaims(String sid, String refreshJti) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sid", sid);
        claims.put("jti", refreshJti);
        claims.put("type", "refresh");
        return claims;
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

    private OAuthProfile mapGoogle(Map<String, Object> attributes) {
        String providerId = stringValue(attributes.get("sub"));
        String name = stringValue(attributes.get("name"));
        String email = stringValue(attributes.get("email"));

        if (!StringUtils.hasText(providerId)) {
            throw new BadCredentialsException("OAUTH_PROFILE_INVALID");
        }

        return new OAuthProfile("google", providerId, email, name);
    }

    @SuppressWarnings("unchecked")
    private OAuthProfile mapNaver(Map<String, Object> attributes) {
        Object responseObject = attributes.get("response");
        if (!(responseObject instanceof Map<?, ?> responseMap)) {
            throw new BadCredentialsException("OAUTH_PROFILE_INVALID");
        }

        Map<String, Object> response = (Map<String, Object>) responseMap;

        String providerId = stringValue(response.get("id"));
        String name = stringValue(response.get("name"));
        String email = stringValue(response.get("email"));

        if (!StringUtils.hasText(providerId)) {
            throw new BadCredentialsException("OAUTH_PROFILE_INVALID");
        }

        return new OAuthProfile("naver", providerId, name, email);
    }

    @SuppressWarnings("unchecked")
    private OAuthProfile mapKakao(Map<String, Object> attributes) {
        String providerId = stringValue(attributes.get("id"));

        String name = null;
        String email = null;

        Object kakaoAccountObject = attributes.get("kakao_account");
        if (kakaoAccountObject instanceof Map<?, ?> kakaoAccountMap) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoAccountMap;

            email = stringValue(kakaoAccount.get("email"));

            Object profileObject = kakaoAccount.get("profile");
            if (profileObject instanceof Map<?, ?> profileMap) {
                Map<String, Object> profile = (Map<String, Object>) profileMap;
                name = stringValue(profile.get("nickname"));
            }
        }

        if (!StringUtils.hasText(providerId)) {
            throw new BadCredentialsException("OAUTH_PROFILE_INVALID");
        }

        return new OAuthProfile("kakao", providerId, name, email);
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }

    @Override
    public String normalizeProvider(String provider) {
        return normalize(provider);
    }

    private String normalizeNullable(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim().toLowerCase();
    }

    private String normalizeDisplayName(String value) {
        if (!StringUtils.hasText(value)) {
            return "Social User";
        }
        return value.trim();
    }

    private String resolveStatus(AuthUserProfileInfo profileInfo, AuthAccount account) {
        if (profileInfo != null && StringUtils.hasText(profileInfo.getStatus())) {
            return profileInfo.getStatus().trim().toUpperCase();
        }

        if (account != null && StringUtils.hasText(account.getStatus())) {
            return account.getStatus().trim().toUpperCase();
        }

        return "INACTIVE";
    }

    private String resolveFullName(AuthUserProfileInfo profileInfo, AuthAccount account) {
        if (profileInfo != null && StringUtils.hasText(profileInfo.getFullName())) {
            return profileInfo.getFullName();
        }

        if (account != null && StringUtils.hasText(account.getFullName())) {
            return account.getFullName();
        }

        return account == null ? null : account.getUsername();
    }
}
