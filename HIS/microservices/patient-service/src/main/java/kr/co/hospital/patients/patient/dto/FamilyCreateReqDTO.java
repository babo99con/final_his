package kr.co.hospital.patients.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Schema(description = "??? ?? ??")
public class FamilyCreateReqDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "??(?, ? ?)", required = true)
    private String relation;
    @Schema(description = "??? ??", required = true)
    private String familyName;
    @Schema(description = "??? ???")
    private String familyPhone;
    @Schema(description = "????")
    private LocalDate birthDate;
    @Schema(description = "? ??? ??")
    private Boolean isPrimary;
}
