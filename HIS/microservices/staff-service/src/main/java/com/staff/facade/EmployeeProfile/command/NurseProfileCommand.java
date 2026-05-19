package com.staff.facade.EmployeeProfile.command;

import com.staff.domain.employee.nurse.dto.NurseRequestDTO;


//간호사 임시레코드
public record NurseProfileCommand(String staffId,
                                  String licenseNo,
                                  String shiftType,
                                  String education,
                                  String careerDetail,
                                  String extNo) {

    public static NurseProfileCommand from(String staffId, NurseRequestDTO requestDTO) {
        if (requestDTO == null) return null;
        return new NurseProfileCommand(staffId,
                requestDTO.getLicenseNo(),
                requestDTO.getShiftType(),
                requestDTO.getEducation(),
                requestDTO.getCareerDetail(),
                requestDTO.getExtNo());
    }
    public NurseRequestDTO toRequestDTO() {
        return NurseRequestDTO.builder().
                staffId(staffId).
                licenseNo(licenseNo).
                shiftType(shiftType).
                education(education).
                careerDetail(careerDetail).
                extNo(extNo).build();
    }
}
