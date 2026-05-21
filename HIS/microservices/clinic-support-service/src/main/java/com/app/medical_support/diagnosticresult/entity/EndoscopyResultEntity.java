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
@Table(schema = "HOSPITAL", name = "ENDOSCOPY_EXAM_RESULT")
@Getter
@Setter
@NoArgsConstructor
public class EndoscopyResultEntity {

    @Id
    @Column(name = "ENDOSCOPY_RESULT_ID")
    private String endoscopyResultId;

    @Column(name = "ENDOSCOPY_EXAM_ID")
    private String endoscopyExamId;

    @Column(name = "RESULT_SUMMARY")
    private String resultSummary;

    @Column(name = "BIOPSY_YN")
    private String biopsyYn;

    @Column(name = "CONFIRMED_AT")
    private LocalDateTime confirmedAt;

    @Column(name = "RESULT_MANAGER_ID")
    private String resultManagerId;

    @Column(name = "RESULT_MANAGER_NAME")
    private String resultManagerName;

    @Column(name = "READER_ID")
    private String readerId;

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
