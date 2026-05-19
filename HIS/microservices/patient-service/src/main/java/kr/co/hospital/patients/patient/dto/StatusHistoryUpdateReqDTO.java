package kr.co.hospital.patients.patient.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusHistoryUpdateReqDTO {
    private String fromStatus;
    private String toStatus;
    private String reason;
    private String changedBy;
}