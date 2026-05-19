package com.app.medical_support.common.stafflookup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StaffOptionDTO {
    private String staffId;
    private String fullName;
    private String dutyCode;
}
