package com.app.medical_support.diagnosticresult.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TestResultSearchCondition {

    private String resultType;
    /** 결과 PK (예: imagingResultId) */
    private String resultId;
    private String patientName;
    private String detailCode;
    private String departmentName;
    private String status;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    private Boolean includeInactive;
}
