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
public class PhysiologicalResultDTO {

    private String physiologicalExamResultId;
    private String physiologicalExamId;
    private String testExecutionId;
    private String detailCode;
    private Long patientId;
    private String patientName;
    private String departmentName;
    private String examEquipmentId;
    private String rawData;
    private String reportDocId;
    private String performerId;
    private String performerName;
    private String resultManagerId;
    private String resultManagerName;
    private String resultSummary;
    private String report;
    private String measuredItemCode;
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

    public PhysiologicalResultDTO(
            String physiologicalExamResultId,
            String physiologicalExamId,
            String testExecutionId,
            String detailCode,
            Long patientId,
            String patientName,
            String departmentName,
            String examEquipmentId,
            String rawData,
            String reportDocId,
            String performerId,
            String performerName,
            String resultManagerId,
            String resultManagerName,
            String resultSummary,
            String report,
            String measuredItemCode,
            LocalDateTime confirmedAt,
            String status,
            String progressStatus,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime completedAt
    ) {
        this.physiologicalExamResultId = physiologicalExamResultId;
        this.physiologicalExamId = physiologicalExamId;
        this.testExecutionId = testExecutionId;
        this.detailCode = detailCode;
        this.patientId = patientId;
        this.patientName = patientName;
        this.departmentName = departmentName;
        this.examEquipmentId = examEquipmentId;
        this.rawData = rawData;
        this.reportDocId = reportDocId;
        this.performerId = performerId;
        this.performerName = performerName;
        this.resultManagerId = resultManagerId;
        this.resultManagerName = resultManagerName;
        this.resultSummary = resultSummary;
        this.report = report;
        this.measuredItemCode = measuredItemCode;
        this.confirmedAt = confirmedAt;
        this.status = status;
        this.progressStatus = progressStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
    }
}
