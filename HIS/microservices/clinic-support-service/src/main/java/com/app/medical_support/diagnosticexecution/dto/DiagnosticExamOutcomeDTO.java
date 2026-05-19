package com.app.medical_support.diagnosticexecution.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DiagnosticExamOutcomeDTO {
    private String examKind;
    private String examId;
    private String testExecutionId;
    private Long orderItemId;
    private String progressStatus;
    private Long patientId;
    private Long visitId;
    private String detailCode;
}

