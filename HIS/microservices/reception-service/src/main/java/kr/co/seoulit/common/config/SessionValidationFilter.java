package kr.co.seoulit.common.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
        boolean cookieMissing = !StringUtils.hasText(cookie);
        if (cookieMissing) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "AUTH_SESSION_REQUIRED");
            return;
        }

        boolean sessionValid = isSessionValid(cookie);
        if (!sessionValid) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "AUTH_SESSION_REQUIRED");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isSessionValid(String cookie) throws IOException {
        URL validateUrl = new URL(sessionValidationUrl);
        HttpURLConnection connection = (HttpURLConnection) validateUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", cookie);
        connection.setRequestProperty("Accept", "application/json");
        connection.setConnectTimeout(2000);
        connection.setReadTimeout(3000);
        int status = connection.getResponseCode();
        connection.disconnect();
        boolean successStatus = status >= 200 && status < 300;

        return successStatus;
    }

    private boolean shouldSkip(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        // 상태 확인과 Swagger 문서는 로그인 전에도 접근 가능해야 하므로 세션 검증을 건너뜁니다.
        String path = request.getRequestURI();
        boolean rootPath = path.equals("/");
        boolean actuatorPath = path.startsWith("/actuator");
        boolean swaggerPath = path.startsWith("/swagger");
        boolean swaggerDocsPath = path.startsWith("/v3/api-docs") || path.startsWith("/api-docs");

        return rootPath || actuatorPath || swaggerPath || swaggerDocsPath;
    }
}
