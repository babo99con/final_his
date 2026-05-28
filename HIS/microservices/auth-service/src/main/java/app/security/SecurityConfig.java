package app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    private static final String[] MEDICAL_SUPPORT_ROLES = {
            "ADMIN",
            "DOCTOR",
            "NURSE",
            "RADIOLOGY_TECH",
            "CLINICAL_LAB_TECH",
            "PATHOLOGY_COORDINATOR",
            "ENDOSCOPY_COORDINATOR",
            "PHYSIOLOGY_TEST_COORDINATOR"
    };

    private final ServerRoleFilter serverRoleFilter;
    private final MenuAccessFilter menuAccessFilter;

    public SecurityConfig(ServerRoleFilter serverRoleFilter,
                          MenuAccessFilter menuAccessFilter) {
        this.serverRoleFilter = serverRoleFilter;
        this.menuAccessFilter = menuAccessFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**")
                )
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/auth/register/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/api-docs/**",
                        "/v3/api-docs/**"
                ).permitAll()
                .antMatchers("/api/auth/me", "/api/auth/logout").authenticated()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/menus").authenticated()
                .antMatchers("/api/admin/permissions/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/jpa/training/certificates/verify").permitAll()
                .antMatchers(HttpMethod.POST, "/api/jpa/training/*/complete").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/jpa/training/*/completed-members").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/jpa/training/*/members").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/jpa/training/*/members/*/status").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/medical/**").hasAnyRole(MEDICAL_SUPPORT_ROLES)
                .antMatchers("/api/medical/**").hasAnyRole(MEDICAL_SUPPORT_ROLES)
                .antMatchers("/api/jpa/departments/**", "/api/jpa/positions/**").hasRole("ADMIN")
                .antMatchers("/api/jpa/staff-credentials/**").hasAnyRole(MEDICAL_SUPPORT_ROLES)
                .antMatchers("/api/jpa/staff-change-requests/**").hasRole("ADMIN")
                .antMatchers("/api/jpa/staff-audit-logs/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/jpa/medical-staff/me").authenticated()
                .antMatchers(HttpMethod.GET, "/api/jpa/medical-staff/me").authenticated()
                .antMatchers(HttpMethod.PATCH, "/api/jpa/medical-staff/me/photo").authenticated()
                .antMatchers(HttpMethod.PATCH, "/api/jpa/medical-staff/me/password").authenticated()
                .antMatchers(HttpMethod.POST, "/api/jpa/medical-staff/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/jpa/medical-staff/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/jpa/medical-staff/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/jpa/medical-staff/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/jpa/medical-staff/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(serverRoleFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(menuAccessFilter, ServerRoleFilter.class);

        return http.build();
    }
}
