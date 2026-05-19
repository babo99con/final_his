package com.app.medical_support.common.integration.claims.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimsRequest {
    private String eventId;
    private Long visitId;
    private Long patientId;
    private String status;
    private String occurredAt;
    private List<ClaimsItemRequest> items;
}
