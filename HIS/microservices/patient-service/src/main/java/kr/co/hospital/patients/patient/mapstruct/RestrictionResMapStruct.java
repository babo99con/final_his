package kr.co.hospital.patients.patient.mapstruct;

import kr.co.hospital.patients.common.mapper.EntityResMapper;
import kr.co.hospital.patients.patient.dto.RestrictionResDTO;
import kr.co.hospital.patients.patient.entity.RestrictionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestrictionResMapStruct extends EntityResMapper<RestrictionEntity, RestrictionResDTO> {
    @Override
    RestrictionResDTO toDTO(RestrictionEntity entity);

    @Override
    List<RestrictionResDTO> toDTOList(List<RestrictionEntity> entities);
}
