package com.app.medical_support.diagnosticresult.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestResultUpdateReqDTO {

    private String status;
    private LocalDateTime confirmedAt;
    private String resultManagerId;
    private String resultManagerName;
    private TestResultUpdateDetailDTO detail;
}
