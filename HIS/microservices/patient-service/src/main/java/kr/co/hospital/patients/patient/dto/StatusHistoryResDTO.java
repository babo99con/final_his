package kr.co.hospital.patients.patient.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StatusHistoryResDTO {
    private Long historyId;
    private Long patientId;
    private String fromStatus;
    private String toStatus;
    private String reason;
    private String changedBy;
    private LocalDateTime changedAt;
}
