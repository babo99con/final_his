package app.auth.verification.controller;

import app.auth.register.dto.RegisterContactSendRequest;
import app.auth.register.dto.RegisterContactVerifyRequest;
import app.auth.register.dto.RegisterContactVerifyResponse;
import app.auth.verification.dto.EmailSendRequest;
import app.auth.verification.dto.EmailVerifyRequest;
import app.auth.verification.dto.PhoneSendRequest;
import app.auth.verification.dto.PhoneVerifyRequest;
import app.auth.verification.service.VerificationService;
import com.hms.util.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/register/email/send")
    public ResponseEntity<ApiResponse<Void>> sendRegisterEmailCode(@RequestBody RegisterContactSendRequest request) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(verificationService.sendRegisterEmailCode(request.getValue())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/register/email/verify")
    public ResponseEntity<ApiResponse<RegisterContactVerifyResponse>> verifyRegisterEmailCode(@RequestBody RegisterContactVerifyRequest request) {
        try {
            String token = verificationService.verifyRegisterEmailCode(request.getValue(), request.getCode());
            return ResponseEntity.ok(ApiResponse.ok(new RegisterContactVerifyResponse(token)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/register/phone/send")
    public ResponseEntity<ApiResponse<Void>> sendRegisterPhoneCode(@RequestBody RegisterContactSendRequest request) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(verificationService.sendRegisterPhoneCode(request.getValue())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/register/phone/verify")
    public ResponseEntity<ApiResponse<RegisterContactVerifyResponse>> verifyRegisterPhoneCode(@RequestBody RegisterContactVerifyRequest request) {
        try {
            String token = verificationService.verifyRegisterPhoneCode(request.getValue(), request.getCode());
            return ResponseEntity.ok(ApiResponse.ok(new RegisterContactVerifyResponse(token)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/email/send")
    public ResponseEntity<ApiResponse<Void>> sendVerificationEmail(@RequestBody EmailSendRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("AUTH_UNAUTHORIZED"));
        }
        try {
            return ResponseEntity.ok(ApiResponse.ok(verificationService.sendVerificationCode(authentication.getName(), request.getEmail())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/email/verify")
    public ResponseEntity<ApiResponse<Boolean>> verifyEmailCode(@RequestBody EmailVerifyRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("AUTH_UNAUTHORIZED"));
        }
        try {
            boolean verified = verificationService.verifyCode(authentication.getName(), request.getEmail(), request.getCode());
            if (!verified) {
                return ResponseEntity.badRequest().body(ApiResponse.error("AUTH_EMAIL_CODE_MISMATCH"));
            }
            return ResponseEntity.ok(ApiResponse.ok(true));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/phone/send")
    public ResponseEntity<ApiResponse<Void>> sendVerificationPhone(@RequestBody PhoneSendRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("AUTH_UNAUTHORIZED"));
        }
        try {
            return ResponseEntity.ok(ApiResponse.ok(verificationService.sendPhoneVerificationCode(authentication.getName(), request.getPhone())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/phone/verify")
    public ResponseEntity<ApiResponse<Boolean>> verifyPhoneCode(@RequestBody PhoneVerifyRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("AUTH_UNAUTHORIZED"));
        }
        try {
            boolean verified = verificationService.verifyPhoneCode(authentication.getName(), request.getPhone(), request.getCode());
            if (!verified) {
                return ResponseEntity.badRequest().body(ApiResponse.error("AUTH_PHONE_CODE_MISMATCH"));
            }
            return ResponseEntity.ok(ApiResponse.ok(true));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
