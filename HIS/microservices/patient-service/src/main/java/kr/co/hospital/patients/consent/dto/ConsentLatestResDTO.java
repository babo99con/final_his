package kr.co.hospital.patients.consent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "??? ?? ??")
public class ConsentLatestResDTO {
    @Schema(description = "???ID")
    private Long consentId;
    @Schema(description = "??? ??")
    private String consentType;
    @Schema(description = "?? ??")
    private Boolean activeYn;
    @Schema(description = "?? ??")
    private LocalDateTime agreedAt;
    @Schema(description = "?? ??")
    private LocalDateTime withdrawnAt;
}
