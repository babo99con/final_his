package com.app.medical_support.common.messaging;

import com.app.medical_support.diagnosticexecution.dto.TestExecutionReqDTO;
import org.springframework.stereotype.Component;

/**
 * Clinical 서비스에서 들어오는 검사 수행자(performer) 필드를 저장 규칙에 맞게 정규화합니다.
 * 현재는 널/공백 처리만 수행하며, 필요 시 매핑 룰을 확장합니다.
 */
@Component
public class TestExecutionPerformerInboundNormalizer {

    public void applyForClinicalKafka(TestExecutionReqDTO dto) {
        if (dto == null) {
            return;
        }
        dto.setPerformerId(trimToNull(dto.getPerformerId()));
        dto.setPerformerName(trimToNull(dto.getPerformerName()));
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}