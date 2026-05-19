package com.staff.domain.employee.doctor.validator;

import com.staff.common.exception.BusinessException;
import com.staff.domain.employee.basicInfo.validator.StaffCommonValidator;
import com.staff.domain.employee.doctor.dto.DoctorRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoctorValidator {
    //예외처리 (공통)
    private final StaffCommonValidator staffCommonValidator;


    //닥터용 예외처리 (검증용)
    public void validateDoctorId(String staffId) {
        staffCommonValidator.requireText(staffId, "의사 STAFF_ID가 없습니다.");
    }


    //닥터 가입용
    public void validateCreateRequest(DoctorRequestDTO requestDTO) {
        if (requestDTO == null) throw new BusinessException("의사 등록 요청이 없습니다.");


        staffCommonValidator.requireText(requestDTO.getLicenseNo(), "의사 면허번호는 필수입니다.");
    }


    //의사 수정용
    public void validateUpdateRequest(String staffId, DoctorRequestDTO requestDTO) {
        validateDoctorId(staffId);
        if (requestDTO == null) throw new BusinessException("의사 수정 요청이 없습니다.");

        staffCommonValidator.requireText(requestDTO.getLicenseNo(), "의사 면허번호는 필수입니다.");
    }
}
