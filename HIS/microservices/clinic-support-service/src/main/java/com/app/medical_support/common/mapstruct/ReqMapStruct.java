package com.app.medical_support.common.mapstruct;
//요청할 때는 dto에서 entity 로 변환해서 보내기


import com.app.medical_support.diagnosticexecution.dto.SpecimenDTO;
import com.app.medical_support.diagnosticexecution.entity.SpecimenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

//@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReqMapStruct<E, D>{

    E toEntity(D d) ;

}


