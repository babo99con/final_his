package kr.co.hospital.patients.patient.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusHistoryCreateReqDTO {
    private Long patientId;
    private String fromStatus;
    private String toStatus;
    private String reason;
    private String changedBy;
}
