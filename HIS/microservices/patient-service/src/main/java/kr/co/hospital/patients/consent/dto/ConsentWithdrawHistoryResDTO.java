package kr.co.hospital.patients.consent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "??? ?? ??")
public class ConsentWithdrawHistoryResDTO {
    @Schema(description = "?? ID")
    private Long historyId;
    @Schema(description = "??? ID")
    private Long consentId;
    @Schema(description = "??? ??")
    private String consentType;
    @Schema(description = "?? ??")
    private LocalDateTime withdrawnAt;
    @Schema(description = "???")
    private String changedBy;
    @Schema(description = "?? ??")
    private LocalDateTime createdAt;
}
