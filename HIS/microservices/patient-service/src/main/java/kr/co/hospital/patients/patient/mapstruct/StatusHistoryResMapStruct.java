package kr.co.hospital.patients.patient.mapstruct;

import kr.co.hospital.patients.common.mapper.EntityResMapper;
import kr.co.hospital.patients.patient.dto.StatusHistoryResDTO;
import kr.co.hospital.patients.patient.entity.StatusHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StatusHistoryResMapStruct
        extends EntityResMapper<StatusHistoryEntity, StatusHistoryResDTO> {
    @Override
    StatusHistoryResDTO toDTO(StatusHistoryEntity entity);
}