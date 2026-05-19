package com.app.medical_support.diagnosticexecution.mapstruct;

import com.app.medical_support.common.mapstruct.ResMapStruct;
import com.app.medical_support.diagnosticexecution.dto.PathologyDTO;
import com.app.medical_support.diagnosticexecution.entity.PathologyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PathologyResMapStruct extends ResMapStruct<PathologyDTO, PathologyEntity> {
}
