package com.app.medical_support.nursingtreatment.mapstruct;

import com.app.medical_support.nursingtreatment.dto.RecordDTO;
import com.app.medical_support.nursingtreatment.entity.RecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecordResMapStruct {

    RecordDTO toDTO(RecordEntity entity);
    List<RecordDTO> toDTOList(List<RecordEntity> entities) ;
}
