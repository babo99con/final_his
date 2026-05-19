package com.staff.facade.EmployeeDelete.facade;

import com.staff.domain.employee.basicInfo.dto.StaffResponseDTO;
import com.staff.facade.EmployeeDelete.command.BasicInfoDelete;

public interface EmployeeDeleteFacade {



    //공통 영구삭제 (하드삭제)
    StaffResponseDTO deleteStaff(BasicInfoDelete command);

//
//    void deleteNurse(String staffId);
//
//
//    //삭제
//    void deleteDoctor(String staffId);//서비스에
}
