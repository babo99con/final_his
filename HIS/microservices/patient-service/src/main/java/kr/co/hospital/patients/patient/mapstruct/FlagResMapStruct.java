package kr.co.hospital.patients.patient.mapstruct;

import kr.co.hospital.patients.common.mapper.EntityResMapper;
import kr.co.hospital.patients.patient.dto.FlagResDTO;
import kr.co.hospital.patients.patient.entity.FlagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FlagResMapStruct extends EntityResMapper<FlagEntity, FlagResDTO> {
    @Override
    FlagResDTO toDTO(FlagEntity entity);

    @Override
    List<FlagResDTO> toDTOList(List<FlagEntity> entities);
}