package com.app.medical_support.common.integration.claims.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClaimsResultResponse {
    private Long billingRequestId;
    private Long billId;
    private boolean alreadyProcessed;
    private String message;
}
