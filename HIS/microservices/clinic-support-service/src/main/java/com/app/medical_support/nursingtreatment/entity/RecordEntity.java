package com.app.medical_support.nursingtreatment.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Table(name = "NURSING_RECORD", schema = "CHJ")
@Getter
@Setter
@NoArgsConstructor
public class RecordEntity {

    @Id
    @Column(name = "RECORD_ID")
    private String recordId;

    @Column(name = "SYSTOLIC_BP")
    private Integer systolicBp;

    @Column(name = "DIASTOLIC_BP")
    private Integer diastolicBp;

    @Column(name = "PULSE")
    private Integer pulse;

    @Column(name = "RESPIRATION")
    private Integer respiration;

    @Column(name = "TEMPERATURE")
    private Double temperature;

    @Column(name = "SPO2")
    private Integer spo2;

    @Column(name = "OBSERVATION")
    private String observation;

    @Column(name = "PAIN_SCORE")
    private Integer painScore;

    @Column(name = "CONSCIOUSNESS_LEVEL")
    private String consciousnessLevel;

    @Column(name = "INITIAL_ASSESSMENT")
    private String initialAssessment;

    @Column(name = "PAST_MEDICAL_HISTORY")
    private String pastMedicalHistory;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "RECEPTION_ID")
    private Long receptionId;

    @Column(name = "NURSING_ID")
    private String nursingId;

    @Column(name = "NURSE_NAME")
    private String nurseName;

    @Column(name = "HEIGHT_CM")
    private String heightCm;

    @Column(name = "WEIGHT_KG")
    private String weightKg;

}
