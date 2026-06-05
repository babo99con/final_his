package com.app.medical_support.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

// 이 서비스의 보호 API는 auth-service가 검증한 JSESSIONID 세션 쿠키를 기준으로 접근을 허용합니다.
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "진료 지원 서비스 API 문서",
                description = "검사 실행, 검사 결과, 처치, 투약 기록과 진료 지원 연동 기능을 제공하는 서비스입니다. 보호 API는 auth-service에서 검증한 JSESSIONID 세션 쿠키가 필요합니다.",
                version = "v1"
        ),
        servers = @Server(url = "/", description = "현재 서버"),
        security = @SecurityRequirement(name = "JSESSIONID")
)
@SecurityScheme(
        name = "JSESSIONID",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.COOKIE,
        paramName = "JSESSIONID",
        description = "로그인 후 발급되는 서버 세션 쿠키입니다. 세션이 없거나 만료되면 401 응답이 반환됩니다."
)
public class OpenApiConfig {
}
