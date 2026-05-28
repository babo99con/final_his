package com.example.hospitalClinical.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new JdkClientHttpRequestFactory());
        restTemplate.getInterceptors().add((request, body, execution) -> {
            String cookie = currentCookieHeader();
            if (StringUtils.hasText(cookie)) {
                request.getHeaders().set(HttpHeaders.COOKIE, cookie);
            }
            return execution.execute(request, body);
        });
        return restTemplate;
    }

    private String currentCookieHeader() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return null;
        }
        return attributes.getRequest().getHeader(HttpHeaders.COOKIE);
    }
}
