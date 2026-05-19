package app.auth.oauth.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Value("${app.oauth.redirect-failure:}")
    private String failureRedirect;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String message = exception == null ? "oauth_login_failed" : exception.getMessage();
        String redirectBase = StringUtils.hasText(failureRedirect)
                ? failureRedirect
                : request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/login";
        response.sendRedirect(redirectBase + "?oauthError=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
    }
}
