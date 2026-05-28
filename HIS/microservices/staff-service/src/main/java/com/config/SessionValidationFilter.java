package com.config;

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
        String path = request.getRequestURI();
        return path.equals("/")
                || path.startsWith("/actuator")
                || path.startsWith("/swagger")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/api-docs");
    }
}
