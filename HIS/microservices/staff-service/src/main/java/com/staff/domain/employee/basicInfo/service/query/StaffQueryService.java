package com.staff.domain.employee.basicInfo.service.query;

import com.staff.domain.employee.basicInfo.dto.StaffResponseDTO;

import java.util.List;



/*** 조회는 MyBatis Mapper를 사용.*/
public interface StaffQueryService {


    StaffResponseDTO detailStaff(String staffId);


    List<StaffResponseDTO> listStaff();


    List<StaffResponseDTO> searchStaff(String search, String searchType);

}
