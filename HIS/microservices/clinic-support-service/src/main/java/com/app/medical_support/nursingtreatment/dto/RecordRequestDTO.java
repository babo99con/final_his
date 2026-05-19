package com.app.medical_support.nursingtreatment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RecordRequestDTO {

    private String recordId;
    private Long receptionId;
    private String status;
    private String observation;
    private Integer systolicBp;
    private Integer diastolicBp;
    private Integer pulse;
    private Integer respiration;
    private Double temperature;
    private Integer spo2;
    private Integer painScore;
    private String nursingId;
    private String nurseName;
    private String departmentName;
    private String consciousnessLevel;
    private String initialAssessment;
    private String pastMedicalHistory;
    private String updatedAt;
    private String patientName;
    private String heightCm;
    private String weightKg;
}
