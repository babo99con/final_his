package com.staff.domain.employee.reception.validator;

import com.staff.common.exception.BusinessException;
import com.staff.domain.employee.basicInfo.validator.StaffCommonValidator;
import com.staff.domain.employee.reception.dto.ReceptionRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReceptionValidator {
    private final StaffCommonValidator staffCommonValidator;




    public void validateReceptionId(String staffId) {
        staffCommonValidator.requireText(staffId, "원무 STAFF_ID가 없습니다.");
    }

    public void validateCreateRequest(ReceptionRequestDTO requestDTO) {
        if (requestDTO == null) throw new BusinessException("원무 등록 요청이 없습니다.");

    }

    public void validateUpdateRequest(String staffId, ReceptionRequestDTO requestDTO) {
        validateReceptionId(staffId);
        if (requestDTO == null) throw new BusinessException("원무 수정 요청이 없습니다.");
    }
}
