package com.app.medical_support.common.integration.reception.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 접수 MSA {@code GET /api/doctors} 응답 행 (의사 ID·이름).
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceptionDoctorDTO {

    private String doctorId;
    private String doctorName;
    private String departmentId;
}
