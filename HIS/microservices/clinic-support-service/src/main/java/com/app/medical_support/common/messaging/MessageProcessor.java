package com.app.medical_support.common.messaging;

import com.app.medical_support.common.event.Event;
import com.app.medical_support.common.exceptions.EventProcessingException;
import com.app.medical_support.diagnosticexecution.dto.TestExecutionReqDTO;
import com.app.medical_support.diagnosticexecution.service.DiagnosticExecutionService;
import com.app.medical_support.nursingtreatment.dto.MedicationRecordReqDTO;
import com.app.medical_support.nursingtreatment.dto.TreatmentResultCreateDTO;
import com.app.medical_support.nursingtreatment.service.NursingTreatmentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.time.LocalDateTime;
import java.util.function.Consumer;

@Configuration
public class MessageProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessor.class);

    @Bean
    public Consumer<Message<byte[]>> messageProcessorMedicationRecord(
            NursingTreatmentService nursingTreatmentService,
            ObjectMapper objectMapper
    ) {
        return message -> {
            EventEnvelope event = parseEnvelope(objectMapper, message);
            LOG.info("Process message created at {}...", event.eventCreatedAt);

            switch (event.eventType) {
                case CREATE:
                    MedicationRecordReqDTO dto = objectMapper.convertValue(event.data, MedicationRecordReqDTO.class);
                    String medicationId = dto != null ? dto.getMedicationId() : event.keyText;
                    LOG.info("Create medicationRecord with ID: {}", medicationId);
                    nursingTreatmentService.registerMedicationRecord(dto);
                    break;
                case DELETE:
                    String medicationIdForDelete = event.keyText;
                    LOG.info("Delete medicationRecord with ID: {}", medicationIdForDelete);
                    nursingTreatmentService.updateMedicationRecordStatus(medicationIdForDelete, "INACTIVE");
                    break;
                default:
                    String errorMessage =
                            "Incorrect event type: " + event.eventType + ", expected a CREATE or DELETE event";
                    LOG.warn(errorMessage);
                    throw new EventProcessingException(errorMessage);
            }

            LOG.info("Message processing done!");
        };
    }

    @Bean
    public Consumer<Message<byte[]>> messageProcessorTreatmentResult(
            NursingTreatmentService nursingTreatmentService,
            ObjectMapper objectMapper
    ) {
        return message -> {
            EventEnvelope event = parseEnvelope(objectMapper, message);
            LOG.info("Process message created at {}...", event.eventCreatedAt);

            switch (event.eventType) {
                case CREATE:
                    TreatmentResultCreateDTO dto = objectMapper.convertValue(event.data, TreatmentResultCreateDTO.class);
                    String procedureResultId = dto != null ? dto.getProcedureResultId() : event.keyText;
                    LOG.info("Create treatmentResult with ID: {}", procedureResultId);
                    nursingTreatmentService.registerTreatmentResult(dto);
                    break;
                case DELETE:
                    String procedureResultIdForDelete = event.keyText;
                    LOG.info("Delete treatmentResult with ID: {}", procedureResultIdForDelete);
                    nursingTreatmentService.updateTreatmentResultStatus(procedureResultIdForDelete, "INACTIVE");
                    break;
                default:
                    String errorMessage =
                            "Incorrect event type: " + event.eventType + ", expected a CREATE or DELETE event";
                    LOG.warn(errorMessage);
                    throw new EventProcessingException(errorMessage);
            }

            LOG.info("Message processing done!");
        };
    }

    @Bean
    public Consumer<Message<byte[]>> messageProcessorTestExecution(
            DiagnosticExecutionService diagnosticExecutionService,
            ObjectMapper objectMapper,
            TestExecutionPerformerInboundNormalizer testExecutionPerformerInboundNormalizer
    ) {
        return message -> {
            EventEnvelope event = parseEnvelope(objectMapper, message);
            LOG.info("Process message created at {}...", event.eventCreatedAt);

            switch (event.eventType) {
                case CREATE:
                    TestExecutionReqDTO dto = objectMapper.convertValue(event.data, TestExecutionReqDTO.class);
                    Long orderItemId = event.keyLong;
                    LOG.info("Create testExecution with OrderItemID: {}", orderItemId);
                    if (dto != null && dto.getOrderItemId() == null && orderItemId != null) {
                        dto.setOrderItemId(orderItemId);
                    }
                    if (dto != null) {
                        testExecutionPerformerInboundNormalizer.applyForClinicalKafka(dto);
                    }
                    diagnosticExecutionService.registerTestExecution(dto);
                    break;
                case DELETE:
                    Long orderItemIdForDelete = event.keyLong;
                    LOG.info("Delete testExecution with OrderItemID: {}", orderItemIdForDelete);
                    // NOTE: 현재 DiagnosticExecutionService에는 testExecution delete가 없어 무시합니다.
                    break;
                default:
                    String errorMessage =
                            "Incorrect event type: " + event.eventType + ", expected a CREATE or DELETE event";
                    LOG.warn(errorMessage);
                    throw new EventProcessingException(errorMessage);
            }

            LOG.info("Message processing done!");
        };
    }

    private static EventEnvelope parseEnvelope(ObjectMapper objectMapper, Message<byte[]> message) {
        try {
            byte[] payload = message.getPayload();
            if (payload == null) {
                throw new IllegalArgumentException("Kafka message payload is null");
            }
            // Avoid byte[] -> String roundtrip to prevent encoding/garbling issues.
            JsonNode root = objectMapper.readTree(payload);

            String eventTypeRaw = text(root.get("eventType"));
            Event.Type eventType = eventTypeRaw != null ? Event.Type.valueOf(eventTypeRaw) : null;

            JsonNode keyNode = root.get("key");
            String keyText = keyNode != null && !keyNode.isNull() ? keyNode.asText() : null;
            Long keyLong = null;
            if (keyNode != null && keyNode.isNumber()) {
                keyLong = keyNode.asLong();
            } else if (keyText != null) {
                try {
                    keyLong = Long.parseLong(keyText);
                } catch (NumberFormatException ignored) {
                    keyLong = null;
                }
            }

            JsonNode data = root.get("data");
            LocalDateTime eventCreatedAt = null;
            String createdAtRaw = text(root.get("eventCreatedAt"));
            if (createdAtRaw != null) {
                eventCreatedAt = LocalDateTime.parse(createdAtRaw);
            }

            return new EventEnvelope(eventType, keyText, keyLong, data, eventCreatedAt);
        } catch (Exception e) {
            String errorMessage = "Failed to parse inbound kafka event payload. reason=" + e.getMessage();
            LOG.warn(errorMessage, e);
            throw new EventProcessingException(errorMessage);
        }
    }

    private static String text(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        String v = node.asText();
        if (v == null) {
            return null;
        }
        String t = v.trim();
        return t.isEmpty() ? null : t;
    }

    private static final class EventEnvelope {
        final Event.Type eventType;
        final String keyText;
        final Long keyLong;
        final JsonNode data;
        final LocalDateTime eventCreatedAt;

        private EventEnvelope(Event.Type eventType, String keyText, Long keyLong, JsonNode data, LocalDateTime eventCreatedAt) {
            this.eventType = eventType;
            this.keyText = keyText;
            this.keyLong = keyLong;
            this.data = data;
            this.eventCreatedAt = eventCreatedAt;
        }
    }
}

