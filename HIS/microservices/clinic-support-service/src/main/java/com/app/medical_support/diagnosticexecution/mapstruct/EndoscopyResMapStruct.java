package com.app.medical_support.diagnosticexecution.mapstruct;

import com.app.medical_support.common.mapstruct.ResMapStruct;
import com.app.medical_support.diagnosticexecution.dto.EndoscopyDTO;
import com.app.medical_support.diagnosticexecution.entity.EndoscopyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EndoscopyResMapStruct extends ResMapStruct<EndoscopyDTO, EndoscopyEntity> {
}
