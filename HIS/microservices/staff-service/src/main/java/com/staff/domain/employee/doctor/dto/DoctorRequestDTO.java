package com.staff.domain.employee.doctor.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequestDTO {

    // 공통 직원 정보
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

    // 의사 상세 정보
    private String licenseNo;
    private String specialtyId;
    private String doctorType;
    private String doctorFileUrl;
    private String profileSummary;
    private String education;
    private String careerDetail;
    private String extNo;
}
