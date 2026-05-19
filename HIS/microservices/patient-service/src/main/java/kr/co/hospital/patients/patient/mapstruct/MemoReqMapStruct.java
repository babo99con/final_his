package kr.co.hospital.patients.patient.mapstruct;

import kr.co.hospital.patients.common.mapper.EntityReqMapper;
import kr.co.hospital.patients.patient.dto.MemoCreateReqDTO;
import kr.co.hospital.patients.patient.entity.MemoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemoReqMapStruct extends EntityReqMapper<MemoEntity, MemoCreateReqDTO> {
    @Override
    MemoEntity toEntity(MemoCreateReqDTO dto);
}
