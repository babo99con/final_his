package kr.co.hospital.patients.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "?? ?? ?? ??")
public class PatientIdentifyResDTO {
    @Schema(description = "?? ??(STRONG, MEDIUM, WEAK)")
    private String matchLevel;
    @Schema(description = "?? ??")
    private PatientResDTO patient;
}
