package kr.co.hospital.patients.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "?? ?? ?? ??")
public class PatientIdentifyReqDTO {
    @Schema(description = "?? ??")
    private String name;
    @Schema(description = "????(YYYY-MM-DD)")
    private String birthDate;
    @Schema(description = "???")
    private String phone;
}
