package com.staff.domain.employee.basicInfo.service.command;

import com.staff.domain.employee.basicInfo.dto.StaffRequestDTO;
import com.staff.domain.employee.basicInfo.dto.StaffResponseDTO;
import com.staff.domain.employee.basicInfo.dto.StaffUpdateRequestDTO;

/**직원 공통 쓰기 서비스.*/
public interface StaffCommonService {

    //공통 가입
    StaffResponseDTO createStaff(StaffRequestDTO staffReq);
    //공통 수정

    StaffResponseDTO updateStaff(String staffId, StaffUpdateRequestDTO requestDTO);

    //고통 삭제
    StaffResponseDTO deleteStaff(String staffId);





}
