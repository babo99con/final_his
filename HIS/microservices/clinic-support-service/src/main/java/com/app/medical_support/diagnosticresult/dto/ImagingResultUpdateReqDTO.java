package com.app.medical_support.diagnosticresult.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ImagingResultUpdateReqDTO {

    @JsonAlias("readingSummary")
    private String resultSummary;
    private String readingDetail;
    private LocalDateTime confirmedAt;
    private String resultManagerId;
    private String resultManagerName;
    private String status;
}
