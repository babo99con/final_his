package com.app.medical_support.nursingtreatment.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TreatmentResultUpdateDTO {

    private String status;

    @JsonProperty("progress_status")
    @JsonAlias("progressStatus")
    private String progressStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime treatmentAt;

    private String nursingId;
    private String detail;
    private Long patientId;
    private String patientName;
    private String departmentName;
    private String procedureResultId;
    private String nurseName;
}
