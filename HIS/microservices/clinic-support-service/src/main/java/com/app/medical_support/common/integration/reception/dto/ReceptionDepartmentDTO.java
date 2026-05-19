package com.app.medical_support.common.integration.reception.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 접수 MSA {@code GET /api/departments} 응답 행.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceptionDepartmentDTO {

    private String departmentId;
    private String departmentName;
}
