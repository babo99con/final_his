package com.example.hospitalClinical.order.integration.clinicalsupport.inbound;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.support-feedback.kafka")
public class SupportFeedbackKafkaProperties {
    private boolean enabled;
    private String bootstrapServers = "localhost:9092";
    private String groupId = "hospital-clinical-support-feedback";
    // clinic-support-service publishes outcome events to these topics by default.
    private String topicMedication = "medicationRecordOutcome";
    private String topicTreatment = "treatmentResultOutcome";
    private String topicTestExecution = "testExecution";
}
