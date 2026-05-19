package kr.co.hospital.patients.consent.mapstruct;

import kr.co.hospital.patients.common.mapper.EntityResMapper;
import kr.co.hospital.patients.consent.dto.ConsentResDTO;
import kr.co.hospital.patients.consent.entity.ConsentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConsentResMapStruct extends EntityResMapper<ConsentEntity, ConsentResDTO>
{
    @Override
    ConsentResDTO toDTO(ConsentEntity entity) ;
    @Override
    List<ConsentResDTO> toDTOList(List<ConsentEntity> entities) ;



}