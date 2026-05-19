package kr.co.hospital.patients.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "?? ?? ??")
public class ConsentTypeReq {
    @Schema(description = "??", example = "PRIVACY")
    private String code;

    @Schema(description = "??", example = "???? ?? ??")
    private String name;

    @Schema(description = "????", example = "1")
    private Integer sortOrder;

    @Schema(description = "????", example = "true")
    private Boolean isActive;
}
