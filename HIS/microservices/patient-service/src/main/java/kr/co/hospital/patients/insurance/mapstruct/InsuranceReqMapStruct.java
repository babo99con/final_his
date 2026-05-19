package kr.co.hospital.patients.insurance.mapstruct;


import kr.co.hospital.patients.common.mapper.EntityReqMapper;
import kr.co.hospital.patients.insurance.dto.InsuranceCreateReqDTO;
import kr.co.hospital.patients.insurance.entity.InsuranceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InsuranceReqMapStruct extends EntityReqMapper<InsuranceEntity, InsuranceCreateReqDTO>
{
    @Override
    InsuranceEntity toEntity(InsuranceCreateReqDTO dto) ;

}