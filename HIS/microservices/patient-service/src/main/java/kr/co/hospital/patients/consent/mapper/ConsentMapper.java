package kr.co.hospital.patients.consent.mapper;

import kr.co.hospital.patients.consent.entity.ConsentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConsentMapper {

    List<ConsentEntity> search(
            @Param("type") String type,
            @Param("keyword") String keyword
    );
}
