package com.app.medical_support.diagnosticexecution.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ImagingCreateReqDTO {

    private String testExecutionId;
    private String imagingType;
    private String detailCode;
    private Long patientId;
    private String patientName;
    private String departmentName;
    private String status;
    private String progressStatus;
    private String performerId;
    private String performerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
