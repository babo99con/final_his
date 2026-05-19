package com.app.medical_support.integration.outbound.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "clinic-support.downstream.kafka")
public class DownstreamKafkaProperties {

    private boolean enabled = true;
}
