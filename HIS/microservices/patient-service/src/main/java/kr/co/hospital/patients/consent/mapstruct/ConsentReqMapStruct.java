package kr.co.hospital.patients.consent.mapstruct;


import kr.co.hospital.patients.common.mapper.EntityReqMapper;
import kr.co.hospital.patients.consent.dto.ConsentCreateReqDTO;
import kr.co.hospital.patients.consent.entity.ConsentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConsentReqMapStruct extends EntityReqMapper<ConsentEntity, ConsentCreateReqDTO>
{
    @Override
    ConsentEntity toEntity(ConsentCreateReqDTO dto) ;

}