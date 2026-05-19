package com.app.medical_support.diagnosticexecution.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ImagingDTO {

    private String imagingExamId;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
