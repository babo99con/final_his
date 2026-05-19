package com.app.medical_support.diagnosticresult.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestResultUpdateDetailDTO {

    private String readingDetail;

    private String resultItemCode;
    private String unit;
    private String referenceRange;
    private String judgement;

    @JsonAlias({"readingSummary", "resultValue", "finding"})
    private String resultSummary;
    private LocalDateTime judgedAt;
    private String readerId;
    private String diagnosisName;

    private String biopsyYn;

    private String report;
    private String measuredItemCode;
}
