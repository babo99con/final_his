package com.app.medical_support.diagnosticexecution.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(schema = "HOSPITAL", name = "SUPPORT_TEST_EXECUTION")
public class TestExecutionEntity {

    @Id
    @Column(name = "TEST_EXECUTION_ID")
    private String testExecutionId;

    @Column(name = "DETAIL_CODE")
    private String detailCode;

    @Column(name = "ORDER_ITEM_ID")
    private Long orderItemId;

    @Column(name = "EXECUTION_TYPE")
    private String executionType;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PROGRESS_STATUS")
    private String progressStatus;

    @Column(name = "RETRY_NO")
    private Integer retryNo;

    @Column(name = "STARTED_AT")
    private LocalDateTime startedAt;

    @Column(name = "COMPLETED_AT")
    private LocalDateTime completedAt;

    @Column(name = "PERFORMER_ID")
    private String performerId;

    @Column(name = "PERFORMER_NAME")
    private String performerName;

    @Column(name = "PATIENT_ID")
    private Long patientId;

    @Column(name = "PATIENT_NAME")
    private String patientName;

    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
