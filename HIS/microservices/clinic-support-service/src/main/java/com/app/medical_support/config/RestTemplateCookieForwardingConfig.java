package com.app.medical_support.config;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class RestTemplateCookieForwardingConfig {

    @Bean
    public RestTemplateCustomizer sessionCookieForwardingCustomizer() {
        return restTemplate -> restTemplate.getInterceptors().add((request, body, execution) -> {
            String cookie = currentCookieHeader();
            if (StringUtils.hasText(cookie)) {
                request.getHeaders().set(HttpHeaders.COOKIE, cookie);
            }
            return execution.execute(request, body);
        });
    }

    private String currentCookieHeader() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return null;
        }
        return attributes.getRequest().getHeader(HttpHeaders.COOKIE);
    }
}
