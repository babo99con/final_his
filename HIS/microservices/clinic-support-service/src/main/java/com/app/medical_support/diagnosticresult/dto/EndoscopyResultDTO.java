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
public class EndoscopyResultDTO {

    private String endoscopyResultId;
    private String endoscopyExamId;
    private String testExecutionId;
    private String detailCode;
    private Long patientId;
    private String patientName;
    private String departmentName;
    private String performerId;
    private String performerName;
    private String resultManagerId;
    private String resultManagerName;
    private String resultSummary;
    private String biopsyYn;
    private LocalDateTime confirmedAt;
    private String readerId;
    private String status;
    private String progressStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    @JsonProperty("isRevised")
    private boolean isRevised;

    public EndoscopyResultDTO(
            String endoscopyResultId,
            String endoscopyExamId,
            String testExecutionId,
            String detailCode,
            Long patientId,
            String patientName,
            String departmentName,
            String performerId,
            String performerName,
            String resultManagerId,
            String resultManagerName,
            String resultSummary,
            String biopsyYn,
            LocalDateTime confirmedAt,
            String readerId,
            String status,
            String progressStatus,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime completedAt
    ) {
        this.endoscopyResultId = endoscopyResultId;
        this.endoscopyExamId = endoscopyExamId;
        this.testExecutionId = testExecutionId;
        this.detailCode = detailCode;
        this.patientId = patientId;
        this.patientName = patientName;
        this.departmentName = departmentName;
        this.performerId = performerId;
        this.performerName = performerName;
        this.resultManagerId = resultManagerId;
        this.resultManagerName = resultManagerName;
        this.resultSummary = resultSummary;
        this.biopsyYn = biopsyYn;
        this.confirmedAt = confirmedAt;
        this.readerId = readerId;
        this.status = status;
        this.progressStatus = progressStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
    }
}
