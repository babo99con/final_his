package kr.co.hospital.patients.patient.mapstruct;


import kr.co.hospital.patients.common.mapper.EntityReqMapper;
import kr.co.hospital.patients.patient.dto.CreateReqDTO;
import kr.co.hospital.patients.patient.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientReqMapStruct extends EntityReqMapper<PatientEntity, CreateReqDTO>
{
    @Override
    PatientEntity toEntity(CreateReqDTO dto) ;

}