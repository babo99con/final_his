package kr.co.hospital.patients.patient.mapstruct;

import kr.co.hospital.patients.common.mapper.EntityReqMapper;
import kr.co.hospital.patients.patient.dto.RestrictionCreateReqDTO;
import kr.co.hospital.patients.patient.entity.RestrictionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestrictionReqMapStruct extends EntityReqMapper<RestrictionEntity, RestrictionCreateReqDTO> {
    @Override
    RestrictionEntity toEntity(RestrictionCreateReqDTO dto);
}
