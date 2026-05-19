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
public class ImagingResultDTO {

    private String imagingResultId;
    private String imagingExamId;
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
    private String readingDetail;
    private LocalDateTime confirmedAt;
    private String status;
    private String progressStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    @JsonProperty("isRevised")
    private boolean isRevised;

    public ImagingResultDTO(
            String imagingResultId,
            String imagingExamId,
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
            String readingDetail,
            LocalDateTime confirmedAt,
            String status,
            String progressStatus,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime completedAt
    ) {
        this.imagingResultId = imagingResultId;
        this.imagingExamId = imagingExamId;
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
        this.readingDetail = readingDetail;
        this.confirmedAt = confirmedAt;
        this.status = status;
        this.progressStatus = progressStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
    }
}
