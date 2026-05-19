package com.app.medical_support.diagnosticexecution.mapstruct;

import com.app.medical_support.common.mapstruct.ReqMapStruct;
import com.app.medical_support.diagnosticexecution.dto.PathologyCreateReqDTO;
import com.app.medical_support.diagnosticexecution.dto.PathologyDTO;
import com.app.medical_support.diagnosticexecution.entity.PathologyEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PathologyReqMapStruct extends ReqMapStruct<PathologyEntity, PathologyCreateReqDTO> {

    @Override
    @Mapping(target = "pathologyExamId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PathologyEntity toEntity(PathologyCreateReqDTO dto);

    @Mapping(target = "pathologyExamId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "progressStatus", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PathologyDTO dto, @MappingTarget PathologyEntity entity);
}
