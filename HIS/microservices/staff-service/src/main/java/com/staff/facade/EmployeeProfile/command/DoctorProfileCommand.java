package com.staff.facade.EmployeeProfile.command;

import com.staff.domain.employee.doctor.dto.DoctorRequestDTO;


//의사 임시레코드
public record DoctorProfileCommand(String staffId,
                                   String licenseNo,
                                   String specialtyId,
                                   String profileSummary,
                                   String education,
                                   String careerDetail,
                                   String extNo) {

    public static DoctorProfileCommand from(String staffId,
                                            DoctorRequestDTO requestDTO) {
        if (requestDTO == null) return null;
        return new DoctorProfileCommand(staffId,
                requestDTO.getLicenseNo(),
                requestDTO.getSpecialtyId(),
                requestDTO.getProfileSummary(),
                requestDTO.getEducation(),
                requestDTO.getCareerDetail(),
                requestDTO.getExtNo());
    }



    public DoctorRequestDTO toRequestDTO() {
        return DoctorRequestDTO.builder().
                staffId(staffId).
                licenseNo(licenseNo).
                specialtyId(specialtyId).
                profileSummary(profileSummary).
                education(education).
                careerDetail(careerDetail).
                extNo(extNo).build();
    }
}
