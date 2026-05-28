package app.auth.me.controller;

import app.auth.common.dto.AuthUserInfo;
import app.auth.me.dto.ChangePasswordRequest;
import app.auth.me.service.MeService;
import app.auth.session.service.SessionService;
import com.hms.util.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "내 정보", description = "로그인한 사용자가 자기 정보를 확인하고 비밀번호를 바꾸는 API입니다.")
public class MeController {

    private final MeService meService;
    private final SessionService sessionService;

    public MeController(MeService meService, SessionService sessionService) {
        this.meService = meService;
        this.sessionService = sessionService;
    }

    @GetMapping("/me")
    @Operation(
            summary = "내 정보 조회",
            description = "현재 로그인한 사람의 이름, 역할, 부서 같은 기본 정보를 가져옵니다."
    )
    public ResponseEntity<ApiResponse<AuthUserInfo>> me(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("AUTH_UNAUTHORIZED"));
        }

        HttpSession session = request.getSession(false);
        if (session == null || !sessionService.isSessionAliveAndTouch(authentication.getName(), session.getId())) {
            SecurityContextHolder.clearContext();
            if (session != null) {
                session.invalidate();
            }
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

    @PatchMapping("/me/password")
    @Operation(
            summary = "내 비밀번호 변경",
            description = "현재 비밀번호를 확인한 뒤 새 비밀번호로 바꿉니다."
    )
    public ResponseEntity<ApiResponse<Void>> changeMyPassword(@RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("AUTH_UNAUTHORIZED"));
        }

        meService.changeMyPassword(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.ok("AUTH_PASSWORD_CHANGED"));
    }
}
