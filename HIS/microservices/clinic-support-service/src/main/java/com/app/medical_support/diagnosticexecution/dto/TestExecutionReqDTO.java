package com.app.medical_support.diagnosticexecution.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "Test execution request")
@Getter
@Setter
@NoArgsConstructor
public class TestExecutionReqDTO {

    @Schema(description = "Progress status")
    private String progressStatus;

    @Schema(description = "Detail Code")
    private String detailCode;

    @Schema(description = "Patient ID")
    private Long patientId;

    @Schema(description = "Patient name")
    private String patientName;

    @Schema(description = "Department name")
    private String departmentName;

    @Schema(description = "Order item ID")
    private Long orderItemId;

    @Schema(description = "Execution type")
    private String executionType;

    @Schema(description = "Retry number")
    private Integer retryNo;

    @Schema(description = "Started at")
    private LocalDateTime startedAt;

    @Schema(description = "Completed at")
    private LocalDateTime completedAt;

    @Schema(description = "Performer ID")
    private String performerId;

    @Schema(description = "Performer name")
    private String performerName;
}
