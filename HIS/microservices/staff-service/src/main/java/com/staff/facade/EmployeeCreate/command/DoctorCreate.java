package com.staff.facade.EmployeeCreate.command;

import com.staff.domain.employee.doctor.dto.DoctorRequestDTO;

//Register 올리다 명부/정보

public record DoctorCreate(
        String staffId,
        String licenseNo,
        String specialtyId,
        String doctorType,
        String doctorFileUrl,
        String profileSummary,
        String education,
        String careerDetail,
        String extNo
) {

    public static DoctorCreate from(DoctorRequestDTO requestDTO) {
        if (requestDTO == null) return null;
        return new DoctorCreate(
                requestDTO.getStaffId(),
                requestDTO.getLicenseNo(),
                requestDTO.getSpecialtyId(),
                requestDTO.getDoctorType(),
                requestDTO.getDoctorFileUrl(),
                requestDTO.getProfileSummary(),
                requestDTO.getEducation(),
                requestDTO.getCareerDetail(),
                requestDTO.getExtNo()
        );
    }







    //생성
    public DoctorRequestDTO toRequestDTO() {
        return DoctorRequestDTO.builder()
                .staffId(staffId)
                .licenseNo(licenseNo)
                .specialtyId(specialtyId)
                .doctorType(doctorType)
                .doctorFileUrl(doctorFileUrl)
                .profileSummary(profileSummary)
                .education(education)
                .careerDetail(careerDetail)
                .extNo(extNo)
                .build();
    }
}
