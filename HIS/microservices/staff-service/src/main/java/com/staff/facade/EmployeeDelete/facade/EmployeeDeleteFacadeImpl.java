package com.staff.facade.EmployeeDelete.facade;

import com.staff.domain.employee.basicInfo.dto.StaffResponseDTO;
import com.staff.domain.employee.basicInfo.service.command.StaffCommonService;
import com.staff.domain.employee.basicInfo.validator.StaffCommonValidator;

import com.staff.facade.EmployeeDelete.command.BasicInfoDelete;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class EmployeeDeleteFacadeImpl implements EmployeeDeleteFacade {

    private final StaffCommonService staffCommonService;
    private final StaffCommonValidator staffCommonValidator;


    //공통 영구삭제 (하드삭제)
    @Override
    @Transactional
    public StaffResponseDTO deleteStaff(BasicInfoDelete command) {
        staffCommonValidator.requireText(command.staffId(), "삭제할 STAFF_ID가 없습니다.");

        // 공통 삭제 서비스가 상세(의사/간호사) 존재 여부를 확인한 뒤
        // 자식 테이블 -> 부모 테이블 순서로 안전하게 삭제한다.
        // 여기서 doctor/nurse 삭제를 또 호출하면
        // 한쪽 프로필이 없는 직원 삭제 시 예외가 발생할 수 있다.

        return staffCommonService.deleteStaff(command.staffId());
    }
}
//    //4의사 삭제 (추후 진행 )
//    @Override
//    @Transactional
//    public void deleteDoctor(String staffId) {
//        doctorValidator.validateDoctorId(staffId);
//
//        doctorCommandService.deleteDoctor(staffId);
//    }

//    //간호사 삭제 (지금은 안씀)
//    @Override
//    @Transactional
//    public void deleteNurse(String staffId) {
//        nurseValidator.validateNurseId(staffId);
//
//        nurseCommandService.deleteNurse(staffId);
//
//    }
//}

