package com.app.medical_support.diagnosticexecution.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class EndoscopyExamSearchCondition {

    private String patientName;
    private String departmentName;
    private String sedationYn;
    private String progressStatus;
    private LocalDate startDate;
    private LocalDate endDate;
}
