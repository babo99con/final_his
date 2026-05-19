package app.auth.oauth.handler;

import app.auth.login.dto.LoginResponse;
import app.auth.login.dto.LoginResult;
import app.auth.oauth.controller.OAuthController;
import app.auth.oauth.entity.OAuthProfile;
import app.auth.oauth.service.OAuthService;
import app.auth.session.service.SessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuthService oAuthService;
    private final SessionService sessionService;

    @Value("${app.oauth.redirect-success:}")
    private String successRedirect;

    @Value("${app.oauth.redirect-failure:}")
    private String failureRedirect;

    public OAuth2AuthenticationSuccessHandler(OAuthService oAuthService,
                                              SessionService sessionService) {
        this.oAuthService = oAuthService;
        this.sessionService = sessionService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String resolvedSuccessRedirect = resolveRedirect(request, successRedirect);
        String resolvedFailureRedirect = resolveRedirect(request, failureRedirect);

        if (!(authentication instanceof OAuth2AuthenticationToken token)) {
            response.sendRedirect(resolvedFailureRedirect + "?oauthError=invalid_authentication");
            return;
        }

        try {
            String provider = token.getAuthorizedClientRegistrationId();
            OAuth2User user = token.getPrincipal();
            OAuthProfile profile = oAuthService.mapProfile(provider, user.getAttributes());

            Object flow = request.getSession(false) == null
                    ? null
                    : request.getSession(false).getAttribute(OAuthController.SESSION_OAUTH_FLOW_KEY);

            if (OAuthController.FLOW_REGISTER_SOCIAL_VERIFY.equals(flow)) {
                String requestedProvider = request.getSession(false) == null
                        ? null
                        : String.valueOf(request.getSession(false).getAttribute(OAuthController.SESSION_OAUTH_REGISTER_PROVIDER_KEY));

                if (request.getSession(false) != null) {
                    request.getSession(false).removeAttribute(OAuthController.SESSION_OAUTH_FLOW_KEY);
                    request.getSession(false).removeAttribute(OAuthController.SESSION_OAUTH_REGISTER_PROVIDER_KEY);
                }

                if (!StringUtils.hasText(requestedProvider) || !requestedProvider.equalsIgnoreCase(provider)) {
                    response.sendRedirect(resolvedFailureRedirect + "?oauthError=social_verification_failed");
                    return;
                }

                String verifyToken = oAuthService.issueSocialVerificationToken(
                        provider,
                        profile.providerId(),
                        profile.name(),
                        profile.email()
                );

                String verifyRedirect = UriComponentsBuilder.fromHttpUrl(resolvedSuccessRedirect)
                        .queryParam("oauth", "register_social_ok")
                        .queryParam("provider", provider)
                        .queryParam("verifyToken", verifyToken)
                        .queryParam("verifiedName", profile.name())
                        .build()
                        .encode()
                        .toUriString();

                response.sendRedirect(verifyRedirect);
                return;
            }

            LoginResult loginResult = oAuthService.loginOrRegisterOAuth(
                    provider,
                    profile.providerId(),
                    profile.email(),
                    profile.name()
            );

            sessionService.addAuthCookie(response, loginResult.getResponse().getAccessToken());
            sessionService.addRefreshCookie(response, loginResult.getRefreshToken());

            LoginResponse loginResponse = loginResult.getResponse();

            String redirectUrl = UriComponentsBuilder.fromHttpUrl(resolvedSuccessRedirect)
                    .queryParam("oauth", "ok")
                    .queryParam("token", loginResponse.getAccessToken())
                    .build()
                    .encode()
                    .toUriString();

            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            response.sendRedirect(resolvedFailureRedirect + "?oauthError=oauth_login_failed");
        }
    }

    private String resolveRedirect(HttpServletRequest request, String configuredRedirect) {
        if (StringUtils.hasText(configuredRedirect)) {
            return configuredRedirect;
        }

        String origin = request.getHeader("Origin");
        if (!StringUtils.hasText(origin)) {
            origin = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        }

        return origin + "/login";
    }
}
