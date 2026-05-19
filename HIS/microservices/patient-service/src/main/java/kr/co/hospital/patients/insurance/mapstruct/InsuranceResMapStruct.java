package kr.co.hospital.patients.insurance.mapstruct;

import kr.co.hospital.patients.common.mapper.EntityResMapper;
import kr.co.hospital.patients.insurance.dto.InsuranceResDTO;
import kr.co.hospital.patients.insurance.entity.InsuranceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InsuranceResMapStruct extends EntityResMapper<InsuranceEntity, InsuranceResDTO>
{
    @Override
    InsuranceResDTO toDTO(InsuranceEntity entity) ;
    @Override
    List<InsuranceResDTO> toDTOList(List<InsuranceEntity> entities) ;



}