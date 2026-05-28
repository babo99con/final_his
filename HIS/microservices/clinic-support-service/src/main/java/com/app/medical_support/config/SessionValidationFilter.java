package com.app.medical_support.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SessionValidationFilter implements Filter {

    // 각 마이크로서비스는 직접 로그인 상태를 판단하지 않고 auth-service의 세션 검증 API에 위임합니다.
    @Value("${app.auth.session-validation-url:http://localhost:8586/api/auth/session/validate}")
    private String sessionValidationUrl;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (shouldSkip(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        // 브라우저가 보낸 JSESSIONID 쿠키를 그대로 auth-service에 전달해서 세션 유효성을 확인합니다.
        String cookie = httpRequest.getHeader("Cookie");
        if (!StringUtils.hasText(cookie) || !isSessionValid(cookie)) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "AUTH_SESSION_REQUIRED");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isSessionValid(String cookie) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(sessionValidationUrl).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", cookie);
        connection.setRequestProperty("Accept", "application/json");
        connection.setConnectTimeout(2000);
        connection.setReadTimeout(3000);
        int status = connection.getResponseCode();
        connection.disconnect();
        return status >= 200 && status < 300;
    }

    private boolean shouldSkip(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        // 상태 확인과 Swagger 문서는 로그인 전에도 접근 가능해야 하므로 세션 검증을 건너뜁니다.
        String path = request.getRequestURI();
        return path.equals("/")
                || path.startsWith("/actuator")
                || path.startsWith("/swagger")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/api-docs");
    }
}
