package com.app.medical_support.diagnosticexecution.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PathologyCreateReqDTO {

    private String testExecutionId;
    private String detailCode;
    private Long patientId;
    private String patientName;
    private String departmentName;
    private String tissueStatus;
    private String collectionMethod;
    private String tissueSite;
    private String tissueType;
    private LocalDateTime collectedAt;
    private String performerId;
    private String performerName;
    private String reexamYn;
    private String status;
    private String progressStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
