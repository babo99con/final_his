package kr.co.hospital.patients.patient.mapstruct;

import kr.co.hospital.patients.common.mapper.EntityResMapper;
import kr.co.hospital.patients.patient.dto.PatientResDTO;
import kr.co.hospital.patients.patient.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientResMapStruct extends EntityResMapper<PatientEntity, PatientResDTO>
{
    @Override
    PatientResDTO toDTO(PatientEntity entity) ;
    @Override
    List<PatientResDTO> toDTOList(List<PatientEntity> entities) ;



}