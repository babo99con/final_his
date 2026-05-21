package com.app.medical_support.diagnosticresult.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(schema = "HOSPITAL", name = "PATHOLOGY_EXAM_RESULT")
@Getter
@Setter
@NoArgsConstructor
public class PathologyResultEntity {

    @Id
    @Column(name = "PATHOLOGY_EXAM_RESULT_ID")
    private String pathologyExamResultId;

    @Column(name = "PATHOLOGY_EXAM_ID")
    private String pathologyExamId;

    @Column(name = "RESULT_SUMMARY")
    private String resultSummary;

    @Column(name = "JUDGED_AT")
    private LocalDateTime judgedAt;

    @Column(name = "CONFIRMED_AT")
    private LocalDateTime confirmedAt;

    @Column(name = "RESULT_MANAGER_ID")
    private String resultManagerId;

    @Column(name = "RESULT_MANAGER_NAME")
    private String resultManagerName;

    @Column(name = "READER_ID")
    private String readerId;

    @Column(name = "DIAGNOSIS_NAME")
    private String diagnosisName;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PROGRESS_STATUS")
    private String progressStatus;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "COMPLETED_AT")
    private LocalDateTime completedAt;
}
