package com.staff.domain.employee.reception.mapstruct;

import com.staff.domain.employee.reception.dto.ReceptionRequestDTO;
import com.staff.domain.employee.reception.entity.ReceptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface ReceptionRequestStruct {

    @Mapping(target = "startDate", source = "startDate", dateFormat = "yyyy-MM-dd")
    ReceptionEntity toEntity(ReceptionRequestDTO req);
}
