package app.auth.oauth.controller;

import app.auth.oauth.service.OAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth/oauth")
public class OAuthController {

    public static final String SESSION_OAUTH_FLOW_KEY = "OAUTH_FLOW";
    public static final String SESSION_OAUTH_REGISTER_PROVIDER_KEY = "OAUTH_REGISTER_PROVIDER";
    public static final String FLOW_REGISTER_SOCIAL_VERIFY = "REGISTER_SOCIAL_VERIFY";

    private final OAuthService oAuthService;

    public OAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping("/{provider}/register/start")
    public void startSocialRegisterVerification(@PathVariable String provider,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
        try {
            String normalized = oAuthService.normalizeProvider(provider);
            request.getSession(true).setAttribute(SESSION_OAUTH_FLOW_KEY, FLOW_REGISTER_SOCIAL_VERIFY);
            request.getSession(true).setAttribute(SESSION_OAUTH_REGISTER_PROVIDER_KEY, normalized);
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location", "/oauth2/authorization/" + normalized);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
