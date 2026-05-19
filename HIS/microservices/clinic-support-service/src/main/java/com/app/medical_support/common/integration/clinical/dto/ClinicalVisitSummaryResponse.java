package com.app.medical_support.common.integration.clinical.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClinicalVisitSummaryResponse {

    private Long visitId;
    private Long patientId;
    /**
     * 진료 API는 숫자 ID 또는 "DOC-…" 같은 코드 문자열을 줄 수 있어 String 으로 받는다.
     * (Long 전용이면 JSON 이 문자열일 때 역직렬화 실패 → RestTemplate 502)
     */
    private String doctorId;
    private Long receptionId;
    private String visitStatus;
    private String startTime;
    private String endTime;
    private String createdAt;
    private String updatedAt;
}
