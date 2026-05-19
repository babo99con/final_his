package com.app.medical_support.diagnosticexecution.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EndoscopyDTO {

    private String endoscopyExamId;
    private String testExecutionId;
    private String detailCode;
    private Long patientId;
    private String patientName;
    private String departmentName;
    private String procedureRoom;
    private String equipment;
    private String sedationYn;
    private String performerId;
    private String performerName;
    private LocalDateTime procedureAt;
    private String status;
    private String progressStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
