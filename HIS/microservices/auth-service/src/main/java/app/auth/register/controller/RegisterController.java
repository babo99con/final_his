package app.auth.register.controller;

import app.auth.register.dto.PendingRegisterRequestDto;
import app.auth.register.dto.RegisterRequest;
import app.auth.register.service.RegisterService;
import com.hms.util.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody RegisterRequest request) {
        try {
            registerService.register(request);
            return ResponseEntity.ok(ApiResponse.ok("가입 요청이 접수되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/register/check-username")
    public ResponseEntity<ApiResponse<Boolean>> checkUsername(@RequestParam String username) {
        try {
            boolean available = registerService.isUsernameAvailable(username);

            if (available) {
                return ResponseEntity.ok(ApiResponse.ok("사용 가능한 아이디입니다.", true));
            }

            return ResponseEntity.ok(ApiResponse.ok("이미 존재하는 아이디입니다.", false));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/register-requests")
    public ResponseEntity<ApiResponse<List<PendingRegisterRequestDto>>> readPendingRequests(Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("AUTH_FORBIDDEN"));
        }

        return ResponseEntity.ok(
                ApiResponse.ok(registerService.readPendingRegisterRequests())
        );
    }

    @PostMapping("/register-requests/{accountId}/approve")
    public ResponseEntity<ApiResponse<Void>> approve(@PathVariable String accountId, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("AUTH_FORBIDDEN"));
        }

        registerService.reviewRegisterRequest(accountId, true);
        return ResponseEntity.ok(ApiResponse.ok("가입 요청이 승인되었습니다."));
    }

    @PostMapping("/register-requests/{accountId}/reject")
    public ResponseEntity<ApiResponse<Void>> reject(@PathVariable String accountId, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("AUTH_FORBIDDEN"));
        }

        registerService.reviewRegisterRequest(accountId, false);
        return ResponseEntity.ok(ApiResponse.ok("가입 요청이 거절되었습니다."));
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equalsIgnoreCase(authority.getAuthority()));
    }
}
