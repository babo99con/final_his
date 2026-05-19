package com.app.medical_support.diagnosticexecution.mapstruct;


import com.app.medical_support.common.mapstruct.ResMapStruct;
import com.app.medical_support.diagnosticexecution.dto.ImagingDTO;
import com.app.medical_support.diagnosticexecution.entity.ImagingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface ImagingResMapstruct extends ResMapStruct< ImagingDTO, ImagingEntity> {
}
