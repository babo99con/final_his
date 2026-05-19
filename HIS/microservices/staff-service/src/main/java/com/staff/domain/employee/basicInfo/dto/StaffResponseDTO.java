package com.staff.domain.employee.basicInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 공통 직원 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffResponseDTO {

    //공통
    private String staffId;

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

    //의사

    private String doctorType;
    private String specialtyId;
    private String doctorProfileSummary;
    private String doctorFileUrl;

    
    //간호사

    private String nurseType;
    private String nurseProfileSummary;
    private String nurseFileUrl;

    //원무
    private String receptionType;
}