package com.app.medical_support.nursingtreatment.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class RecordUpdateDTO {

    private Integer systolicBp;
    private Integer diastolicBp;
    private Integer pulse;
    private Integer respiration;
    private Double temperature;
    private Integer spo2;
    private String observation;
    private Integer painScore;
    private String consciousnessLevel;
    private String initialAssessment;
    private String pastMedicalHistory;
    private String status;
    private Long receptionId;
    @Schema(description = "간호사 사용자/직원 ID")
    private String nursingId;

    @Schema(description = "간호사 표시명. 프론트에서 전달 시 NURSE_NAME 컬럼에 저장")
    private String nurseName;
    private String heightCm;
    private String weightKg;
}
