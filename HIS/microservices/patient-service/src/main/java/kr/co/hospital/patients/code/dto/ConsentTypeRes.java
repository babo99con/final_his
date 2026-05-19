package kr.co.hospital.patients.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "??? ?? ??")
public class ConsentTypeRes {
    @Schema(description = "ID")
    private Long id;
    @Schema(description = "??")
    private String code;
    @Schema(description = "???")
    private String name;
    @Schema(description = "????")
    private Integer sortOrder;
    @Schema(description = "????")
    private Boolean isActive;
}
