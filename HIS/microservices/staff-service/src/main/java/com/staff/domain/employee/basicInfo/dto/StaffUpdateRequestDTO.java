package com.staff.domain.employee.basicInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 공통 직원 수정 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffUpdateRequestDTO {

    //공통
    private String deptId;
    private String name;
    private String phone;
    private String email;
    private String birthDate;
    private String genderCode;
    private String zipCode;
    private String address1;
    private String address2;
    private String status;

}