package app.auth.login.controller;

import app.auth.login.dto.LoginRequest;
import app.auth.login.dto.LoginResponse;
import app.auth.login.dto.LoginResult;
import app.auth.login.service.LoginService;
import app.auth.session.service.SessionAuthenticationService;
import com.hms.util.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;
    private final SessionAuthenticationService sessionAuthenticationService;

    public LoginController(LoginService loginService,
                           SessionAuthenticationService sessionAuthenticationService) {
        this.loginService = loginService;
        this.sessionAuthenticationService = sessionAuthenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request,
                                                            HttpServletRequest httpRequest) {
        try {
            LoginResult loginResult = loginService.login(request);
            sessionAuthenticationService.establish(httpRequest, loginResult.getResponse().getUser());

            return ResponseEntity.ok(ApiResponse.ok(loginResult.getResponse()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("AUTH_INVALID_CREDENTIALS"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
