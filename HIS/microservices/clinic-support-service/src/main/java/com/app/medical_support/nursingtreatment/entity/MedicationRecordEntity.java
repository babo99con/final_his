package com.app.medical_support.nursingtreatment.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "HOSPITAL", name = "MEDICATION_RECORD")
@Getter
@Setter
@NoArgsConstructor
public class MedicationRecordEntity {

    @Id
    @Column(name = "MEDICATION_RECORD_ID")
    private String medicationRecordId;

    @Column(name = "MEDICATION_ID")
    private String medicationId;

    @Column(name = "ADMINISTERED_AT")
    private String administeredAt;

    @Column(name = "DOSE_NUMBER")
    private Double doseNumber;

    @Column(name = "DOSE_UNIT")
    private String doseUnit;

    @Column(name = "DOSE_KIND")
    private String doseKind;

    @Column(name = "NURSING_ID")
    private String nursingId;

    @Column(name = "NURSE_NAME")
    private String nurseName;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PROGRESS_STATUS")
    private String progressStatus;

    @Column(name = "CREATED_AT")
    private String createdAt;

    @Column(name = "PATIENT_ID")
    private Long patientId;

    @Column(name = "PATIENT_NAME")
    private String patientName;

    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;
}
