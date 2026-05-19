package com.staff.domain.employee.reception.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceptionRequestDTO {

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


    private String jobTypeCd;
    private String deskNo;
    private String shiftType;
    private String startDate;
    private String windowArea;
    private String multiTask;
    private String rmk;
    private String receptionType;
    private String extNo;
}
