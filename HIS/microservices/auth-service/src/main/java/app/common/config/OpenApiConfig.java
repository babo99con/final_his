package app.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "인증 서비스 API 문서",
                description = "로그인, 로그아웃, 내 정보, 메뉴 권한, 세션 검증을 제공하는 인증 서비스입니다. 로그인 성공 시 서버 세션이 생성되고 이후 요청은 JSESSIONID 쿠키로 인증됩니다.",
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
        description = "로그인 후 발급되는 서버 세션 쿠키입니다. 보호 API 호출 시 이 쿠키가 필요합니다."
)
public class OpenApiConfig {
}
