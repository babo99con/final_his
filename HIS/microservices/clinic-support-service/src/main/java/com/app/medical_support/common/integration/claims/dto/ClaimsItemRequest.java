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
public class ClaimsItemRequest {
    private String itemName;
    private String itemCode;
    private String orderType;
    private Long sourceId;
    private String sourceType;
}
