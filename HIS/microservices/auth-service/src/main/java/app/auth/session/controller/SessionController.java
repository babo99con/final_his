package app.auth.session.controller;

import app.auth.session.service.SessionService;
import com.hms.util.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    Authentication authentication) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        if (authentication != null && authentication.isAuthenticated()) {
            sessionService.invalidateSession(authentication.getName());
        }
        sessionService.clearAuthCookie(response);
        sessionService.clearRefreshCookie(response);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
