package com.app.medical_support.diagnosticexecution.mapstruct;

import com.app.medical_support.common.mapstruct.ReqMapStruct;
import com.app.medical_support.diagnosticexecution.dto.EndoscopyCreateReqDTO;
import com.app.medical_support.diagnosticexecution.dto.EndoscopyDTO;
import com.app.medical_support.diagnosticexecution.entity.EndoscopyEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EndoscopyReqMapStruct extends ReqMapStruct<EndoscopyEntity, EndoscopyCreateReqDTO> {

    @Override
    @Mapping(target = "endoscopyExamId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EndoscopyEntity toEntity(EndoscopyCreateReqDTO dto);

    @Mapping(target = "endoscopyExamId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "progressStatus", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(EndoscopyDTO dto, @MappingTarget EndoscopyEntity entity);
}
