package app.auth.login.controller;

import app.auth.login.dto.LoginRequest;
import app.auth.login.dto.LoginResponse;
import app.auth.login.dto.LoginResult;
import app.auth.login.service.LoginService;
import app.auth.session.service.SessionService;
import com.hms.util.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;
    private final SessionService sessionService;

    public LoginController(LoginService loginService,
                           SessionService sessionService) {
        this.loginService = loginService;
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request,
                                                            HttpServletResponse response) {
        try {
            LoginResult loginResult = loginService.login(request);
            sessionService.addAuthCookie(response, loginResult.getResponse().getAccessToken());
            sessionService.addRefreshCookie(response, loginResult.getRefreshToken());

            return ResponseEntity.ok(ApiResponse.ok(loginResult.getResponse()));
        } catch (BadCredentialsException e) {
            sessionService.clearAuthCookie(response);
            sessionService.clearRefreshCookie(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("AUTH_INVALID_CREDENTIALS"));
        } catch (AccessDeniedException e) {
            sessionService.clearAuthCookie(response);
            sessionService.clearRefreshCookie(response);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(HttpServletRequest request,
                                                              HttpServletResponse response) {
        try {
            String refreshToken = readCookie(request, SessionService.REFRESH_TOKEN_COOKIE);
            LoginResult loginResult = loginService.refresh(refreshToken);
            sessionService.addAuthCookie(response, loginResult.getResponse().getAccessToken());
            sessionService.addRefreshCookie(response, loginResult.getRefreshToken());

            return ResponseEntity.ok(ApiResponse.ok(loginResult.getResponse()));
        } catch (BadCredentialsException e) {
            sessionService.clearAuthCookie(response);
            sessionService.clearRefreshCookie(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (AccessDeniedException e) {
            sessionService.clearAuthCookie(response);
            sessionService.clearRefreshCookie(response);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    private String readCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
