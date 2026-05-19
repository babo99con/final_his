package com.app.medical_support.diagnosticexecution.mapstruct;

import com.app.medical_support.common.mapstruct.ResMapStruct;
import com.app.medical_support.diagnosticexecution.dto.PhysiologicalDTO;
import com.app.medical_support.diagnosticexecution.entity.PhysiologicalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhysiologicalResMapStruct extends ResMapStruct<PhysiologicalDTO, PhysiologicalEntity> {
}
