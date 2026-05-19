package kr.co.hospital.patients.patient.mapstruct;

import kr.co.hospital.patients.common.mapper.EntityResMapper;
import kr.co.hospital.patients.patient.dto.MemoResDTO;
import kr.co.hospital.patients.patient.entity.MemoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemoResMapStruct extends EntityResMapper<MemoEntity, MemoResDTO> {
    @Override
    MemoResDTO toDTO(MemoEntity entity);

    @Override
    List<MemoResDTO> toDTOList(List<MemoEntity> entities);
}
