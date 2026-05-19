package com.app.medical_support.diagnosticexecution.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "Test execution update request")
@Getter
@Setter
@NoArgsConstructor
public class TestExecutionUpdateDTO {

    @Schema(description = "Progress status")
    private String progressStatus;

    @Schema(description = "Status")
    private String status;

    @Schema(description = "Retry count")
    private Integer retryNo;

    @Schema(description = "Completed at")
    private LocalDateTime completedAt;

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

    @Schema(description = "Performer ID")
    private String performerId;

    @Schema(description = "Performer name")
    private String performerName;
}
