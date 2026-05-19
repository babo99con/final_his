package kr.co.hospital.patients.patient.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusChangeReqDTO {
    private String statusCode;
    private String reason;
    private String changedBy;
}
