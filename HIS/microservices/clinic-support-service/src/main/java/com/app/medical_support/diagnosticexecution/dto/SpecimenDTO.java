package com.app.medical_support.diagnosticexecution.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "Specimen exam data")
@Getter
@Setter
@NoArgsConstructor
public class SpecimenDTO {

    @Schema(description = "Specimen exam ID")
    private String specimenExamId;

    @Schema(description = "Test execution ID")
    private String testExecutionId;

    @Schema(description = "Detail code")
    private String detailCode;

    @Schema(description = "Patient ID")
    private Long patientId;

    @Schema(description = "Patient name")
    private String patientName;

    @Schema(description = "Department name")
    private String departmentName;

    @Schema(description = "Specimen type")
    private String specimenType;

    @Schema(description = "Specimen work status")
    private String specimenStatus;

    @Schema(description = "Collected date time")
    private LocalDateTime collectedAt;

    @Schema(description = "Performer ID")
    private String performerId;

    @Schema(description = "Performer name")
    private String performerName;

    @Schema(description = "Collection site")
    private String collectionSite;

    @Schema(description = "Recollection Y/N")
    private String recollectionYn;

    @Schema(description = "Status")
    private String status;

    @Schema(description = "Progress status")
    private String progressStatus;

    @Schema(description = "Created at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Updated at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
