package com.app.medical_support.nursingtreatment.mapstruct;

import com.app.medical_support.nursingtreatment.dto.RecordCreateReqDTO;
import com.app.medical_support.nursingtreatment.entity.RecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecordReqMapStruct {

    RecordEntity toEntity(RecordCreateReqDTO dto) ;
}
