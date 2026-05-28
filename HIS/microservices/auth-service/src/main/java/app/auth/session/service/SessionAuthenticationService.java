package app.auth.session.service;

import app.auth.login.dto.LoginUserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;

@Service
public class SessionAuthenticationService {

    private final SessionService sessionService;

    public SessionAuthenticationService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void establish(HttpServletRequest request, LoginUserDto user) {
        if (request == null || user == null || !StringUtils.hasText(user.getUsername())) {
            return;
        }

        // 로그인 성공 후 Spring Security 인증 정보와 서버 세션을 함께 만들어 이후 요청을 쿠키 기반으로 처리합니다.
        String role = StringUtils.hasText(user.getRole()) ? user.getRole().trim() : "USER";
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        // DB에도 같은 세션 ID를 기록해 마이크로서비스들이 auth-service를 통해 세션 유효성을 확인할 수 있게 합니다.
        sessionService.startSession(user.getUsername(), httpSession.getId());
    }
}
