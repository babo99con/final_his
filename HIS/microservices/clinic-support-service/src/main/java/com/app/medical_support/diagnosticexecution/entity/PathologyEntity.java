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
@Table(schema = "CHJ", name = "PATHOLOGY_EXAM")
@Getter
@Setter
@NoArgsConstructor
public class PathologyEntity {

    @Id
    @Column(name = "PATHOLOGY_EXAM_ID")
    private String pathologyExamId;

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

    @Column(name = "TISSUE_STATUS")
    private String tissueStatus;

    @Column(name = "COLLECTION_METHOD")
    private String collectionMethod;

    @Column(name = "TISSUE_SITE")
    private String tissueSite;

    @Column(name = "TISSUE_TYPE")
    private String tissueType;

    @Column(name = "COLLECTED_AT")
    private LocalDateTime collectedAt;

    @Column(name = "PERFORMER_ID")
    private String performerId;

    @Column(name = "PERFORMER_NAME")
    private String performerName;

    @Column(name = "REEXAM_YN")
    private String reexamYn;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PROGRESS_STATUS")
    private String progressStatus;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
