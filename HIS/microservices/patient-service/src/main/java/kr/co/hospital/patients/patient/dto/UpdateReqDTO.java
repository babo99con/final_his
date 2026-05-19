package kr.co.hospital.patients.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Schema(description = "?? ?? ??")
public class UpdateReqDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "?? ??")
    private String name;
    @Schema(description = "???")
    private String email;
    @Schema(description = "???")
    private String phone;
    @Schema(description = "??")
    private String gender;
    @Schema(description = "????")
    private LocalDate birthDate;
    @Schema(description = "??????(??????? 13??)")
    private String rrn;
    @Schema(description = "??")
    private String address;
    @Schema(description = "????")
    private String addressDetail;

    @Schema(description = "?????")
    private Boolean isForeigner;
    @Schema(description = "????/???? ??")
    private String note;
}
