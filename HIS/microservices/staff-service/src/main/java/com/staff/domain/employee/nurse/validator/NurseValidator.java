package com.staff.domain.employee.nurse.validator;

import com.staff.common.exception.BusinessException;
import com.staff.domain.employee.basicInfo.validator.StaffCommonValidator;
import com.staff.domain.employee.nurse.dto.NurseRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NurseValidator {
    private final StaffCommonValidator staffCommonValidator;

    public void validateNurseId(String staffId) {
        staffCommonValidator.requireText(staffId, "간호사 STAFF_ID가 없습니다.");
    }


    public void validateCreateRequest(NurseRequestDTO requestDTO) {
        if (requestDTO == null) throw new BusinessException("간호사 등록 요청이 없습니다.");

        staffCommonValidator.requireText(requestDTO.getLicenseNo(), "간호사 면허번호는 필수입니다.");
    }

    public void validateUpdateRequest(String staffId, NurseRequestDTO requestDTO) {
        validateNurseId(staffId);
        if (requestDTO == null) throw new BusinessException("간호사 수정 요청이 없습니다.");
        staffCommonValidator.requireText(requestDTO.getLicenseNo(), "간호사 면허번호는 필수입니다.");
    }
}
