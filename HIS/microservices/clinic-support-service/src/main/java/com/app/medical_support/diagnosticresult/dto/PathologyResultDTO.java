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
public class PathologyResultDTO {

    private String pathologyExamResultId;
    private String pathologyExamId;
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
    private String reexamYn;
    private String performerId;
    private String performerName;
    private String resultManagerId;
    private String resultManagerName;
    private String resultSummary;
    private LocalDateTime judgedAt;
    private LocalDateTime confirmedAt;
    private String readerId;
    private String diagnosisName;
    private String status;
    private String progressStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    @JsonProperty("isRevised")
    private boolean isRevised;

    public PathologyResultDTO(
            String pathologyExamResultId,
            String pathologyExamId,
            String testExecutionId,
            String detailCode,
            Long patientId,
            String patientName,
            String departmentName,
            String tissueStatus,
            String collectionMethod,
            String tissueSite,
            String tissueType,
            LocalDateTime collectedAt,
            String reexamYn,
            String performerId,
            String performerName,
            String resultManagerId,
            String resultManagerName,
            String resultSummary,
            LocalDateTime judgedAt,
            LocalDateTime confirmedAt,
            String readerId,
            String diagnosisName,
            String status,
            String progressStatus,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime completedAt
    ) {
        this.pathologyExamResultId = pathologyExamResultId;
        this.pathologyExamId = pathologyExamId;
        this.testExecutionId = testExecutionId;
        this.detailCode = detailCode;
        this.patientId = patientId;
        this.patientName = patientName;
        this.departmentName = departmentName;
        this.tissueStatus = tissueStatus;
        this.collectionMethod = collectionMethod;
        this.tissueSite = tissueSite;
        this.tissueType = tissueType;
        this.collectedAt = collectedAt;
        this.reexamYn = reexamYn;
        this.performerId = performerId;
        this.performerName = performerName;
        this.resultManagerId = resultManagerId;
        this.resultManagerName = resultManagerName;
        this.resultSummary = resultSummary;
        this.judgedAt = judgedAt;
        this.confirmedAt = confirmedAt;
        this.readerId = readerId;
        this.diagnosisName = diagnosisName;
        this.status = status;
        this.progressStatus = progressStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
    }
}
