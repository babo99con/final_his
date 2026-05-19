package com.app.medical_support.nursingtreatment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Schema(description = "Nursing record data")
@Getter
@Setter
@NoArgsConstructor
public class RecordDTO {

    @Id
    @Schema(description = "Record ID")
    private String recordId;

    @Schema(description = "Recorded at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime recordedAt;

    @Schema(description = "Systolic blood pressure")
    private Integer systolicBp;

    @Schema(description = "Diastolic blood pressure")
    private Integer diastolicBp;

    @Schema(description = "Pulse")
    private Integer pulse;

    @Schema(description = "Respiration")
    private Integer respiration;

    @Schema(description = "Temperature")
    private Double temperature;

    @Schema(description = "SpO2")
    private Integer spo2;

    @Schema(description = "Observation")
    private String observation;

    @Schema(description = "Pain score")
    private Integer painScore;

    @Schema(description = "Consciousness level")
    private String consciousnessLevel;

    @Schema(description = "Initial assessment")
    private String initialAssessment;

    @Schema(description = "Past medical history")
    private String pastMedicalHistory;

    @Schema(description = "Status")
    private String status;

    @Schema(description = "Created at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Updated at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "Reception ID")
    private Long receptionId;

    @Schema(description = "Nursing ID")
    private String nursingId;

    @Schema(description = "Nurse display name")
    private String nurseName;

    @Schema(description = "Height (cm)")

    private String heightCm;

    @Schema(description = "Weight (kg)")
    private String weightKg;
}
