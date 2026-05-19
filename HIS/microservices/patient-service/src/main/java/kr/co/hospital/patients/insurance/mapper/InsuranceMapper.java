package kr.co.hospital.patients.insurance.mapper;

import kr.co.hospital.patients.insurance.entity.InsuranceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InsuranceMapper {

    List<InsuranceEntity> search(
            @Param("type") String type,
            @Param("keyword") String keyword
    );
}
