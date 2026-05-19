package com.staff.domain.employee.doctor.mapstruct;

import com.staff.domain.employee.doctor.dto.DoctorRequestDTO;
import com.staff.domain.employee.doctor.entity.DoctorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorReqMapStruct {


//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "updatedAt", ignore = true)
    DoctorEntity toEntity(DoctorRequestDTO req);
}
