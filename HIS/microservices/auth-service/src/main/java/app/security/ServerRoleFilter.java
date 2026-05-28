package app.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ServerRoleFilter extends OncePerRequestFilter {

    @Value("${app.server-role:all}")
    private String serverRole;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String role = serverRole == null ? "all" : serverRole.trim().toLowerCase();
        if ("all".equals(role)) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();
        boolean isAuthPath = path.startsWith("/api/auth");
        boolean isInfraPath = path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/api-docs") || path.startsWith("/error");

        if ("auth".equals(role)) {
            if (!isAuthPath && !isInfraPath) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return;
            }
        } else if ("app".equals(role)) {
            if (isAuthPath) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
