package com.staff.domain.employee.reception.mapper;


import com.staff.domain.employee.reception.dto.ReceptionResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReceptionMapper {


    List<ReceptionResponseDTO> selectReceptionList();


    List<ReceptionResponseDTO> searchReceptionList(@Param("search") String search,
                                           @Param("searchType") String searchType);

    ReceptionResponseDTO selectReceptionById(@Param("staffId") String staffId);

}
