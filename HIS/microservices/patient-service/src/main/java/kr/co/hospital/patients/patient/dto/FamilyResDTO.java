package kr.co.hospital.patients.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Schema(description = "??? ?? ??")
public class FamilyResDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "??? ID")
    private Long familyId;
    @Schema(description = "?? ID")
    private Long patientId;
    @Schema(description = "??(?, ? ?)")
    private String relation;
    @Schema(description = "??? ??")
    private String familyName;
    @Schema(description = "??? ???")
    private String familyPhone;
    @Schema(description = "????")
    private LocalDate birthDate;
    @Schema(description = "? ??? ??")
    private Boolean isPrimary;
    @Schema(description = "?? ??")
    private Integer sortOrder;
}
