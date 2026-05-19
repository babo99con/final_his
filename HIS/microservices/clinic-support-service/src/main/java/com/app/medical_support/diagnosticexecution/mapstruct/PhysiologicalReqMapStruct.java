package com.app.medical_support.diagnosticexecution.mapstruct;

import com.app.medical_support.common.mapstruct.ReqMapStruct;
import com.app.medical_support.diagnosticexecution.dto.PhysiologicalCreateReqDTO;
import com.app.medical_support.diagnosticexecution.dto.PhysiologicalDTO;
import com.app.medical_support.diagnosticexecution.entity.PhysiologicalEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhysiologicalReqMapStruct extends ReqMapStruct<PhysiologicalEntity, PhysiologicalCreateReqDTO> {

    @Override
    @Mapping(target = "physiologicalExamId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PhysiologicalEntity toEntity(PhysiologicalCreateReqDTO dto);

    @Mapping(target = "physiologicalExamId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "progressStatus", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PhysiologicalDTO dto, @MappingTarget PhysiologicalEntity entity);
}
