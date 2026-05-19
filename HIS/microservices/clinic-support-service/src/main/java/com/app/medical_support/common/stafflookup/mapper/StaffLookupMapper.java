package com.app.medical_support.common.stafflookup.mapper;

import com.app.medical_support.common.stafflookup.dto.StaffOptionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StaffLookupMapper {
    List<StaffOptionDTO> findStaffOptions(
            @Param("staffIdPrefix") String staffIdPrefix,
            @Param("dutyCodes") List<String> dutyCodes,
            @Param("keyword") String keyword,
            @Param("limit") int limit
    );
}
