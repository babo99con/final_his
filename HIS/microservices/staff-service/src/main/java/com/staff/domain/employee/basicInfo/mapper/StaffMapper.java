package com.staff.domain.employee.basicInfo.mapper;

import com.staff.domain.employee.basicInfo.dto.StaffResponseDTO;
import com.staff.domain.employee.doctor.dto.DoctorResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/*** 공통 STAFF 조회용 MyBatis Mapper.*/
@Mapper
public interface StaffMapper {

    StaffResponseDTO selectStaffById(@Param("staffId") String staffId);

    List<StaffResponseDTO> selectStaffList();

    List<StaffResponseDTO> searchStaffList(@Param("search") String search,
                                             @Param("searchType") String searchType);

}
