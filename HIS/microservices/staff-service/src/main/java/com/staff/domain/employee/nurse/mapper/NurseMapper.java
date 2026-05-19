package com.staff.domain.employee.nurse.mapper;

import com.staff.domain.employee.nurse.dto.NurseResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface NurseMapper {
    List<NurseResponseDTO> selectNurseList();

    List<NurseResponseDTO> searchNurseList(@Param("search") String search,
                                           @Param("searchType") String searchType);

    NurseResponseDTO selectNurseById(@Param("staffId") String staffId);
}
