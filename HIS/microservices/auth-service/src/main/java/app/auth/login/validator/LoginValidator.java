package app.auth.login.validator;

import app.auth.login.dto.LoginRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class LoginValidator {

    public void validate(LoginRequest request) {
        if (request == null // 요청 자체가 비어있거나
                || !StringUtils.hasText(request.getUsername()) // 들어온 사용자 이름의 값이 비어있거나
                || !StringUtils.hasText(request.getPassword()) // 둘어온 비밀번호의 값이 비어있으면
        ) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
