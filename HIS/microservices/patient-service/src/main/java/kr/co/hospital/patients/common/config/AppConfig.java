package kr.co.hospital.patients.common.config;

import kr.co.hospital.patients.common.storage.MinioProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class AppConfig {
}
