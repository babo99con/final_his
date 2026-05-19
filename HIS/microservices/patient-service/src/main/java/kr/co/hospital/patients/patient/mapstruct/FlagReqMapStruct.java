package kr.co.hospital.patients.patient.mapstruct;

import kr.co.hospital.patients.common.mapper.EntityReqMapper;
import kr.co.hospital.patients.patient.dto.FlagCreateReqDTO;
import kr.co.hospital.patients.patient.entity.FlagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FlagReqMapStruct extends EntityReqMapper<FlagEntity, FlagCreateReqDTO> {
    @Override
    FlagEntity toEntity(FlagCreateReqDTO dto);
}