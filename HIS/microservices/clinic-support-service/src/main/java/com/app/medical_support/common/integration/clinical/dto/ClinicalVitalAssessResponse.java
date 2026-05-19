package com.app.medical_support.common.integration.clinical.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClinicalVitalAssessResponse {

    private Long vitalAssessId;
    private Long visitId;
    private Long receptionId;
    private String recordedAt;
    private Integer systolicBp;
    private Integer diastolicBp;
    private Integer pulse;
    private Integer respiration;
    private Double temperature;
    private Integer spo2;
    private String observation;
    private Integer painScore;
    private String consciousnessLevel;
    private String heightCm;
    private String weightKg;
    private String chiefComplaint;
    private String visitReason;
    private String historyPresentIllness;
    private String pastHistory;
    private String familyHistory;
    private String allergy;
    private String currentMedication;
    private String initialAssessment;
    private String status;
    private String createdAt;
    private String updatedAt;
}
