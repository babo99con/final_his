package com.staff.domain.employee.basicInfo.mapstruct;

import com.staff.domain.employee.basicInfo.dto.StaffRequestDTO;
import com.staff.domain.employee.basicInfo.entity.StaffEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StaffReqMapStruct {

    //참조형
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "nurse", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    StaffEntity toEntity(StaffRequestDTO requestDTO);

}
