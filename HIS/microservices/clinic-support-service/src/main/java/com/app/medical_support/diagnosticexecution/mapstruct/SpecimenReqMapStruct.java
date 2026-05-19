package com.app.medical_support.diagnosticexecution.mapstruct;
//요청할 때는 dto에서 entity 로 변환해서 보내기


import com.app.medical_support.common.mapstruct.ReqMapStruct;
import com.app.medical_support.diagnosticexecution.dto.SpecimenCreateReqDTO;
import com.app.medical_support.diagnosticexecution.entity.SpecimenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpecimenReqMapStruct extends ReqMapStruct<SpecimenEntity, SpecimenCreateReqDTO> {


}
