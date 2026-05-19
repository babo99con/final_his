package com.app.medical_support.diagnosticresult.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TestResultListDTO {

    private String resultType;
    private String resultTypeName;
    private String resultId;
    private String examId;
    private String testExecutionId;
    private String detailCode;
    private Long patientId;
    private String patientName;
    private String departmentName;
    private String tissueStatus;
    private String collectionMethod;
    private String tissueSite;
    private String tissueType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime collectedAt;
    private String reexamYn;
    private String examEquipmentId;
    private String rawData;
    private String reportDocId;
    private String performerId;
    private String performerName;
    private String resultManagerId;
    private String resultManagerName;
    private String summary;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmedAt;
    private String status;
    private String progressStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    @JsonProperty("isRevised")
    private boolean isRevised;
}
