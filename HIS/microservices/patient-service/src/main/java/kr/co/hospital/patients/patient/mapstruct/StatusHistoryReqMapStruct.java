package kr.co.hospital.patients.patient.mapstruct;

import kr.co.hospital.patients.common.mapper.EntityReqMapper;
import kr.co.hospital.patients.patient.dto.StatusHistoryCreateReqDTO;
import kr.co.hospital.patients.patient.entity.StatusHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StatusHistoryReqMapStruct
        extends EntityReqMapper<StatusHistoryEntity, StatusHistoryCreateReqDTO> {
    @Override
    StatusHistoryEntity toEntity(StatusHistoryCreateReqDTO dto);
}