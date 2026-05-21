package com.app.medical_support.diagnosticexecution.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(schema = "HOSPITAL", name = "SPECIMEN_EXAM")
public class SpecimenEntity {

    @Id
    @Column(name = "SPECIMEN_EXAM_ID")
    private String specimenExamId;

    @Column(name = "TEST_EXECUTION_ID")
    private String testExecutionId;

    @Column(name = "DETAIL_CODE")
    private String detailCode;

    @Column(name = "PATIENT_ID")
    private Long patientId;

    @Column(name = "PATIENT_NAME")
    private String patientName;

    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;

    @Column(name = "SPECIMEN_TYPE")
    private String specimenType;

    @Column(name = "SPECIMEN_STATUS")
    private String specimenStatus;

    @Column(name = "COLLECTED_AT")
    private LocalDateTime collectedAt;

    @Column(name = "PERFORMER_ID")
    private String performerId;

    @Column(name = "PERFORMER_NAME")
    private String performerName;

    @Column(name = "COLLECTION_SITE")
    private String collectionSite;

    @Column(name = "RECOLLECTION_YN")
    private String recollectionYn;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PROGRESS_STATUS")
    private String progressStatus;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
