package com.app.medical_support.diagnosticexecution.mapstruct;
//응답할 때는 dto로 변환해서 보내기


import com.app.medical_support.common.mapstruct.ResMapStruct;
import com.app.medical_support.diagnosticexecution.dto.SpecimenDTO;
import com.app.medical_support.diagnosticexecution.entity.SpecimenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpecimenResMapStruct extends ResMapStruct<SpecimenDTO ,SpecimenEntity> {


}

