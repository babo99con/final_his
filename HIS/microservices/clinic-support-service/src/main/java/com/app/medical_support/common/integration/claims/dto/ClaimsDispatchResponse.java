package com.app.medical_support.common.integration.claims.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimsDispatchResponse {
    private Long visitId;
    private Long patientId;
    private String eventId;
    private int itemCount;
    private boolean dispatched;
    private boolean alreadyProcessed;
    private String message;
}
