package com.app.medical_support.nursingtreatment.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TreatmentResultCreateDTO {

    private String status;

    @JsonProperty("progress_status")
    @JsonAlias("progressStatus")
    private String progressStatus;

    private String detail;
    private Long patientId;
    private String patientName;
    private String departmentName;
    private String procedureResultId;
}
