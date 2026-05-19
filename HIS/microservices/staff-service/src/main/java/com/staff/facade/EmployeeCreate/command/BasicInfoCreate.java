package com.staff.facade.EmployeeCreate.command;

import com.staff.domain.employee.basicInfo.dto.StaffRequestDTO;

import com.staff.domain.employee.doctor.dto.DoctorRequestDTO;
import com.staff.domain.employee.nurse.dto.NurseRequestDTO;
import com.staff.domain.employee.reception.dto.ReceptionRequestDTO;

/**
 * 공통 직원 생성용 커맨드 레코드.
 *
 * 목적:
 * 1) 의사/간호사/원무 요청 DTO에서 공통 직원 필드만 추출
 * 2) StaffRequestDTO 로 변환
 *
 * 즉, 퍼사드에서 "공통 직원 생성"을 담당할 때 사용하는 중간 command 역할이다.
 */
public record BasicInfoCreate(
        String staffId,
        String deptId,
        String name,
        String phone,
        String email,
        String birthDate,
        String genderCode,
        String zipCode,
        String address1,
        String address2,
        String status
) {


    public static BasicInfoCreate from(StaffRequestDTO dto) {
        return new BasicInfoCreate(
                dto.getStaffId(),
                dto.getDeptId(),
                dto.getName(),
                dto.getPhone(),
                dto.getEmail(),
                dto.getBirthDate(),
                dto.getGenderCode(),
                dto.getZipCode(),
                dto.getAddress1(),
                dto.getAddress2(),
                dto.getStatus()
        );
    }


    public static BasicInfoCreate from(DoctorRequestDTO Doctordto) {
        if (Doctordto == null) return null;
        return new BasicInfoCreate(
                Doctordto.getStaffId(),
                Doctordto.getDeptId(),
                Doctordto.getName(),
                Doctordto.getPhone(),
                Doctordto.getEmail(),
                Doctordto.getBirthDate(),
                Doctordto.getGenderCode(),
                Doctordto.getZipCode(),
                Doctordto.getAddress1(),
                Doctordto.getAddress2(),
                Doctordto.getStatus()
        );
    }


    public static BasicInfoCreate from(NurseRequestDTO Nursedto) {
        if (Nursedto == null) return null;
        return new BasicInfoCreate(
                Nursedto.getStaffId(),
                Nursedto.getDeptId(),
                Nursedto.getName(),
                Nursedto.getPhone(),
                Nursedto.getEmail(),
                Nursedto.getBirthDate(),
                Nursedto.getGenderCode(),
                Nursedto.getZipCode(),
                Nursedto.getAddress1(),
                Nursedto.getAddress2(),
                Nursedto.getStatus()
        );
    }

    public static BasicInfoCreate from(ReceptionRequestDTO Receptiondto) {
        if (Receptiondto == null) return null;
        return new BasicInfoCreate(
                Receptiondto.getStaffId(),
                Receptiondto.getDeptId(),
                Receptiondto.getName(),
                Receptiondto.getPhone(),
                Receptiondto.getEmail(),
                Receptiondto.getBirthDate(),
                Receptiondto.getGenderCode(),
                Receptiondto.getZipCode(),
                Receptiondto.getAddress1(),
                Receptiondto.getAddress2(),
                Receptiondto.getStatus()
        );
    }








    /**
     * 공통 직원 생성 DTO 로 변환
     */
    public StaffRequestDTO toRequestDTO() {
        return StaffRequestDTO.builder()
                .staffId(staffId)
                .deptId(deptId)
                .name(name)
                .phone(phone)
                .email(email)
                .birthDate(birthDate)
                .genderCode(genderCode)
                .zipCode(zipCode)
                .address1(address1)
                .address2(address2)
                .status(status)
                .build();
    }
}