package com.staff.facade.EmployeeCreate.command;

import com.staff.domain.employee.nurse.dto.NurseRequestDTO;

public record NurseCreate(
        String staffId,
        String licenseNo,
        String nurseType,
        String shiftType,
        String nurseFileUrl,
        String education,
        String careerDetail,
        String extNo
) {
    public static NurseCreate from(NurseRequestDTO requestDTO) {
        if (requestDTO == null) return null;
        return new NurseCreate(
                requestDTO.getStaffId(),
                requestDTO.getLicenseNo(),
                requestDTO.getNurseType(),
                requestDTO.getShiftType(),
                requestDTO.getNurseFileUrl(),
                requestDTO.getEducation(),
                requestDTO.getCareerDetail(),
                requestDTO.getExtNo()
        );
    }

    public NurseRequestDTO toRequestDTO() {
        return NurseRequestDTO.builder()
                .staffId(staffId)
                .licenseNo(licenseNo)
                .nurseType(nurseType)
                .shiftType(shiftType)
                .nurseFileUrl(nurseFileUrl)
                .education(education)
                .careerDetail(careerDetail)
                .extNo(extNo)
                .build();
    }
}
