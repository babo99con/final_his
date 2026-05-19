package com.app.medical_support.integration.outbound.kafka;



import com.app.medical_support.common.event.Event;
import com.app.medical_support.common.integration.clinical.service.ClinicalIntegrationService;
import com.app.medical_support.diagnosticexecution.dto.DiagnosticExamOutcomeDTO;
import com.app.medical_support.diagnosticresult.dto.TestResultDetailDTO;
import com.app.medical_support.nursingtreatment.dto.MedicationRecordDTO;
import com.app.medical_support.nursingtreatment.dto.TreatmentResultDTO;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.stream.function.StreamBridge;

import org.springframework.messaging.support.MessageBuilder;

import org.springframework.stereotype.Component;



import static com.app.medical_support.common.event.Event.Type.CREATE;



/**

 * 진료지원 처리 결과를 Kafka로 보냅니다. 구독 예: 진료(검사결과 등 화면), 수납(검사·처치·투약 내역 정산) 등.

 */

@Component

@RequiredArgsConstructor

@Slf4j

public class DownstreamOutcomeEventPublisher {

    /**
     * StreamBridge 바인딩명은 {@code spring.cloud.stream.output-bindings} 에 등록된 논리 이름을 사용합니다.
     * 실제 destination은 {@code spring.cloud.stream.bindings.<name>-out-0.destination} 으로 매핑됩니다.
     */
    public static final String BINDING_OUT_MEDICATION_RECORD_OUTCOME = "output-medicationRecordOutcome";
    public static final String BINDING_OUT_TREATMENT_RESULT_OUTCOME = "output-treatmentResultOutcome";
    public static final String BINDING_OUT_DIAGNOSTIC_EXAM_OUTCOME = "output-diagnosticExamOutcome";
    public static final String BINDING_OUT_DIAGNOSTIC_TEST_RESULT_OUTCOME = "output-diagnosticTestResultOutcome";



    private final StreamBridge streamBridge;

    private final DownstreamKafkaProperties downstreamKafkaProperties;
    private final ClinicalIntegrationService clinicalIntegrationService;



    public void publishMedicationRecordOutcome(MedicationRecordDTO body) {
        if (!canPublish("medicationRecord", body)) {
            return;
        }
        ensureVisitId(body);

        Object key = body.getMedicationRecordId() != null ? body.getMedicationRecordId() : body.getMedicationId();
        sendWithTrace(
                "medicationRecord",
                BINDING_OUT_MEDICATION_RECORD_OUTCOME,
                key,
                body,
                "medicationRecordId=" + body.getMedicationRecordId()
                        + ", medicationId=" + body.getMedicationId()
                        + ", patientId=" + body.getPatientId()
        );
    }



    public void publishTreatmentResultOutcome(TreatmentResultDTO body) {
        if (!canPublish("treatmentResult", body)) {
            return;
        }
        ensureVisitId(body);

        Object key = body.getTreatmentResultId() != null ? body.getTreatmentResultId() : body.getProcedureResultId();
        sendWithTrace(
                "treatmentResult",
                BINDING_OUT_TREATMENT_RESULT_OUTCOME,
                key,
                body,
                "treatmentResultId=" + body.getTreatmentResultId()
                        + ", procedureResultId=" + body.getProcedureResultId()
                        + ", patientId=" + body.getPatientId()
        );
    }

    /**
     * 검사(실행) 진행상태가 {@code COMPLETED}로 전환되는 시점에 발행합니다.
     */
    public void publishDiagnosticExamOutcome(DiagnosticExamOutcomeDTO body) {
        if (!canPublish("diagnosticExam", body)) {
            return;
        }
        ensureVisitId(body);
        Object key = body.getExamId() != null ? body.getExamId() : body.getTestExecutionId();
        sendWithTrace(
                "diagnosticExam",
                BINDING_OUT_DIAGNOSTIC_EXAM_OUTCOME,
                key,
                body,
                "examKind=" + body.getExamKind()
                        + ", examId=" + body.getExamId()
                        + ", testExecutionId=" + body.getTestExecutionId()
                        + ", patientId=" + body.getPatientId()
                        + ", progressStatus=" + body.getProgressStatus()
        );
    }



    /**

     * 검사 결과가 {@code COMPLETED}로 확정된 뒤 발행합니다. 페이로드는 검사 유형별 상세를 포함합니다.

     */

    public void publishDiagnosticTestResultOutcome(TestResultDetailDTO body) {
        if (!canPublish("diagnosticTestResult", body)) {
            return;
        }
        ensureVisitId(body);

        Object key = body.getResultId() != null ? body.getResultId() : body.getTestExecutionId();
        sendWithTrace(
                "diagnosticTestResult",
                BINDING_OUT_DIAGNOSTIC_TEST_RESULT_OUTCOME,
                key,
                body,
                "resultType=" + body.getResultType()
                        + ", resultId=" + body.getResultId()
                        + ", testExecutionId=" + body.getTestExecutionId()
                        + ", patientId=" + body.getPatientId()
                        + ", progressStatus=" + body.getProgressStatus()
        );
    }

    private boolean canPublish(String eventType, Object body) {
        if (!downstreamKafkaProperties.isEnabled()) {
            log.info("Kafka publish skipped: eventType={}, reason=kafkaDisabled", eventType);
            return false;
        }
        if (body == null) {
            log.warn("Kafka publish skipped: eventType={}, reason=nullBody", eventType);
            return false;
        }
        return true;
    }

    private void sendWithTrace(String eventType, String bindingName, Object key, Object body, String identifiers) {
        try {
            boolean sent = streamBridge.send(
                    bindingName,
                    MessageBuilder.withPayload(new Event<>(CREATE, key, body)).build()
            );
            if (sent) {
                log.info(
                        "Kafka publish success: eventType={}, binding={}, key={}, sent={}, {}",
                        eventType,
                        bindingName,
                        key,
                        true,
                        identifiers
                );
            } else {
                log.warn(
                        "Kafka publish failed: eventType={}, binding={}, key={}, sent={}, {}",
                        eventType,
                        bindingName,
                        key,
                        false,
                        identifiers
                );
            }
        } catch (RuntimeException ex) {
            log.error(
                    "Kafka publish error: eventType={}, binding={}, key={}, {}, message={}",
                    eventType,
                    bindingName,
                    key,
                    identifiers,
                    ex.getMessage(),
                    ex
            );
        }
    }

    private void ensureVisitId(MedicationRecordDTO body) {
        if (body == null || body.getVisitId() != null || body.getPatientId() == null) {
            return;
        }
        Long visitId = clinicalIntegrationService.resolveVisitIdByPatientId(body.getPatientId());
        body.setVisitId(visitId);
    }

    private void ensureVisitId(TreatmentResultDTO body) {
        if (body == null || body.getVisitId() != null || body.getPatientId() == null) {
            return;
        }
        Long visitId = clinicalIntegrationService.resolveVisitIdByPatientId(body.getPatientId());
        body.setVisitId(visitId);
    }

    private void ensureVisitId(DiagnosticExamOutcomeDTO body) {
        if (body == null || body.getVisitId() != null || body.getPatientId() == null) {
            return;
        }
        Long visitId = clinicalIntegrationService.resolveVisitIdByPatientId(body.getPatientId());
        body.setVisitId(visitId);
    }

    private void ensureVisitId(TestResultDetailDTO body) {
        if (body == null || body.getVisitId() != null || body.getPatientId() == null) {
            return;
        }
        Long visitId = clinicalIntegrationService.resolveVisitIdByPatientId(body.getPatientId());
        body.setVisitId(visitId);
    }

}

