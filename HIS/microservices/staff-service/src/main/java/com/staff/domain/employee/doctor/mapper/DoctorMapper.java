package com.staff.domain.employee.doctor.mapper;

import com.staff.domain.employee.doctor.dto.DoctorResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DoctorMapper {

    List<DoctorResponseDTO> selectDoctorList();

    List<DoctorResponseDTO> searchDoctorList(@Param("search") String search,
                                             @Param("searchType") String searchType);

    DoctorResponseDTO selectDoctorById(@Param("staffId") String staffId);


}
