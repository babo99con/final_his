package com.app.medical_support.diagnosticexecution.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "Test execution data")
@Getter
@Setter
@NoArgsConstructor
public class TestExecutionDTO {

    @Schema(description = "Test execution ID")
    private String testExecutionId;

    @Schema(description = "Detail Code")
    private String detailCode;

    @Schema(description = "Order item ID")
    private Long orderItemId;

    @Schema(description = "Execution type")
    private String executionType;

    @Schema(description = "Status")
    private String status;

    @Schema(description = "Progress status")
    private String progressStatus;

    @Schema(description = "Retry count")
    private Integer retryNo;

    @Schema(description = "Completed at")
    private LocalDateTime completedAt;

    @Schema(description = "Performer ID")
    private String performerId;

    @Schema(description = "Performer name")
    private String performerName;

    @Schema(description = "Patient ID")
    private Long patientId;

    @Schema(description = "Patient name")
    private String patientName;

    @Schema(description = "Department name")
    private String departmentName;

    @Schema(description = "Created at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Updated at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
