package com.staff.domain.employee.basicInfo.mapstruct;

import com.staff.domain.employee.basicInfo.dto.StaffResponseDTO;
import com.staff.domain.employee.basicInfo.entity.StaffEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StaffResMapStruct {

    StaffResponseDTO toDTO(StaffEntity entity);
}
