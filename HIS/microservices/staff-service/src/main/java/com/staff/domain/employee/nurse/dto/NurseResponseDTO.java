package com.staff.domain.employee.nurse.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NurseResponseDTO {
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

    private String licenseNo;
    private String nurseType;
    private String shiftType;
    private String nurseFileUrl;
    private String education;
    private String careerDetail;
    private String extNo;
}
