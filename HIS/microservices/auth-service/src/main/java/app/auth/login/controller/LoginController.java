package app.auth.login.controller;

import app.auth.login.dto.LoginRequest;
import app.auth.login.dto.LoginResponse;
import app.auth.login.dto.LoginResult;
import app.auth.login.service.LoginService;
import app.auth.session.service.SessionAuthenticationService;
import com.hms.util.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "로그인", description = "아이디와 비밀번호로 로그인하고 서버 세션을 만드는 API입니다.")
public class LoginController {

    private final LoginService loginService;
    private final SessionAuthenticationService sessionAuthenticationService;

    public LoginController(LoginService loginService,
                           SessionAuthenticationService sessionAuthenticationService) {
        this.loginService = loginService;
        this.sessionAuthenticationService = sessionAuthenticationService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "로그인",
            description = "아이디와 비밀번호가 맞으면 로그인됩니다. 성공하면 브라우저에 JSESSIONID 세션 쿠키가 저장됩니다."
    )
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request,
                                                            HttpServletRequest httpRequest) {
        try {
            LoginResult loginResult = loginService.login(request);
            LoginResponse loginResponse = loginResult.getResponse();
            sessionAuthenticationService.establish(httpRequest, loginResponse.getUser());

            ApiResponse<LoginResponse> responseBody = ApiResponse.ok(loginResponse);

            return ResponseEntity.ok(responseBody);
        } catch (BadCredentialsException e) {
            ApiResponse<LoginResponse> errorBody = ApiResponse.error("AUTH_INVALID_CREDENTIALS");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
        } catch (AccessDeniedException e) {
            ApiResponse<LoginResponse> errorBody = ApiResponse.error(e.getMessage());

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBody);
        }
    }
}
