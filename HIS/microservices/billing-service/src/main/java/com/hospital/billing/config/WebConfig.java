package com.hospital.billing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        // 브라우저에서 Next 등 프론트(다른 포트)가 billing API를 직접 호출할 때 필요 (예: 토스 success URL의 /api/billing/toss/approve).
        // 패턴으로 localhost/127.0.0.1 임의 포트 허용 — 운영 배포 전에는 오리진을 좁히는 것을 권장합니다.
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:*", "http://127.0.0.1:*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }
}