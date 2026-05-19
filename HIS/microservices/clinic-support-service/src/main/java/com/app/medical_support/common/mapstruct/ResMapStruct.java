package com.app.medical_support.common.mapstruct;
//응답할 때는 dto로 변환해서 보내기


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

//@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResMapStruct<D,E> {

//    SpecimenDTO toDTO(SpecimenEntity entity) ;
    D toDTO (E e);
//    List<SpecimenDTO> toDTOList(List<SpecimenEntity> entities) ;
    List<D> toDTOList(List<E> e_list) ;
}

