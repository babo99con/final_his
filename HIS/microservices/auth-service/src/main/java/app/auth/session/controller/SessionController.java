package app.auth.session.controller;

import app.auth.common.dto.AuthUserInfo;
import app.auth.me.service.MeService;
import app.auth.session.service.SessionService;
import com.hms.util.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class SessionController {

    private final SessionService sessionService;
    private final MeService meService;

    public SessionController(SessionService sessionService, MeService meService) {
        this.sessionService = sessionService;
        this.meService = meService;
    }

    @GetMapping("/session/validate")
    public ResponseEntity<ApiResponse<AuthUserInfo>> validate(HttpServletRequest request,
                                                              Authentication authentication) {
        // 다른 마이크로서비스가 Cookie 헤더를 전달해 호출하는 세션 검증 엔드포인트입니다.
        HttpSession session = request.getSession(false);
        if (session == null || authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("AUTH_UNAUTHORIZED"));
        }

        if (!sessionService.isSessionAliveAndTouch(authentication.getName(), session.getId())) {
            SecurityContextHolder.clearContext();
            session.invalidate();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("AUTH_SESSION_EXPIRED"));
        }

        AuthUserInfo userInfo = meService.getCurrentUserInfo(authentication.getName());
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("AUTH_UNAUTHORIZED"));
        }
        return ResponseEntity.ok(ApiResponse.ok(userInfo));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request,
                                                    Authentication authentication) {
        // 로그아웃 시 브라우저 세션과 DB 세션 상태를 같이 정리합니다.
        HttpSession session = request.getSession(false);
        if (session != null) {
            if (authentication != null && authentication.isAuthenticated()) {
                sessionService.invalidateSession(authentication.getName(), session.getId());
            }
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
