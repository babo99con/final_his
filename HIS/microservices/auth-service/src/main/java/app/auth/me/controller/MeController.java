package app.auth.me.controller;

import app.auth.common.dto.AuthUserInfo;
import app.auth.me.dto.ChangePasswordRequest;
import app.auth.me.service.MeService;
import com.hms.util.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class MeController {

    private final MeService meService;

    public MeController(MeService meService) {
        this.meService = meService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthUserInfo>> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("AUTH_UNAUTHORIZED"));
        }

        AuthUserInfo userInfo = meService.getCurrentUserInfo(authentication.getName());
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("AUTH_UNAUTHORIZED"));
        }
        return ResponseEntity.ok(ApiResponse.ok(userInfo));
    }

    @PatchMapping("/me/password")
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
