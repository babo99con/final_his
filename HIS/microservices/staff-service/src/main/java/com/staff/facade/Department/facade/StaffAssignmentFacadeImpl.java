package com.staff.facade.Department.facade;

import com.staff.domain.employee.basicInfo.validator.StaffCommonValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffAssignmentFacadeImpl implements StaffAssignmentFacade {

    private final StaffCommonValidator staffCommonValidator;

    @Override public void assignDoctorToDepartment(String staffId, String departmentId)
    { staffCommonValidator.requireText(staffId, "배정할 의사 STAFF_ID가 없습니다.");
        staffCommonValidator.requireText(departmentId, "배정할 부서 ID가 없습니다."); }

    @Override public void assignNurseToUnit(String staffId, String unitId)
    { staffCommonValidator.requireText(staffId, "배정할 간호사 STAFF_ID가 없습니다.");
        staffCommonValidator.requireText(unitId, "배정할 근무 유닛이 없습니다."); }
}
