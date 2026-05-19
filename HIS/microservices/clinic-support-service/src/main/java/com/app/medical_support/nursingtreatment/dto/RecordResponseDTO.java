package com.app.medical_support.nursingtreatment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RecordResponseDTO {

    private String recordId;
    private Long receptionId;
    private String status;
    private String respiration;
    private String observation;
    private Integer systolicBp;
    private Integer diastolicBp;
    private Integer pulse;
    private Double temperature;
    private Integer spo2;
    private String nursingId;
    private String nurseName;
    private String departmentName;
    private String consciousnessLevel;
    private String initialAssessment;
    private String pastMedicalHistory;
    private String updatedAt;
    private Long patientId;
    private String patientName;
    private String painScore;
    private String heightCm;
    private String weightKg;
    private String createdAt;
}
